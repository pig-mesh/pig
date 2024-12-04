package com.pig4cloud.pigx.common.idempotent.aspect;

import com.pig4cloud.pigx.common.idempotent.annotation.Idempotent;
import com.pig4cloud.pigx.common.idempotent.exception.IdempotentException;
import com.pig4cloud.pigx.common.idempotent.expression.KeyResolver;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.Redisson;
import org.redisson.api.RMapCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * IdempotentAspect: 拦截带有 @Idempotent 注解的方法，提供幂等性控制
 *
 * 功能包括：
 * 1. 根据 URL 或自定义 Key 标识唯一性。
 * 2. 通过 Redisson 的分布式缓存机制，控制请求幂等性。
 * 3. 可根据业务配置 Key 的有效期和是否自动删除等选项。
 *
 * 使用方式：
 * 1. 在方法上添加 @Idempotent 注解。
 * 2. 配置 Redisson 和 KeyResolver 实现类。
 *
 * @author 冷冷
 */
@Aspect
@RequiredArgsConstructor
public class IdempotentAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(IdempotentAspect.class);

    /** 线程缓存，用于存储当前线程的幂等性信息 */
    private static final ThreadLocal<Map<String, Object>> THREAD_CACHE = ThreadLocal.withInitial(HashMap::new);

    /** Redisson Map 缓存的键 */
    private static final String RMAPCACHE_KEY = "idempotent";

    /** 缓存中的 Key 标识 */
    private static final String KEY = "key";

    /** 是否自动删除 Key 标识 */
    private static final String DELKEY = "delKey";

    private final Redisson redisson;

    private final KeyResolver keyResolver;


    /**
     * 定义切点：拦截带有 @Idempotent 注解的方法
     */
    @Pointcut("@annotation(com.pig4cloud.pigx.common.idempotent.annotation.Idempotent)")
    public void pointCut() {
        // Pointcut for methods annotated with @Idempotent
    }

    /**
     * 方法执行前的拦截逻辑
     * - 检查是否存在幂等性 Key，若不存在则抛出异常。
     * - 若首次访问，则存储幂等性 Key 到缓存中。
     *
     * @param joinPoint 切点信息
     */
    @Before("pointCut()")
    public void beforePointCut(JoinPoint joinPoint) {
        // 获取当前请求对象
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        // 获取方法签名和目标方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 检查方法是否有 @Idempotent 注解
        if (!method.isAnnotationPresent(Idempotent.class)) {
            return;
        }

        // 获取注解配置信息
        Idempotent idempotent = method.getAnnotation(Idempotent.class);
        String key = resolveKey(idempotent, joinPoint, request);

        // 获取注解中的其他配置
        long expireTime = idempotent.expireTime();
        String info = idempotent.info();
        TimeUnit timeUnit = idempotent.timeUnit();
        boolean delKey = idempotent.delKey();

        // 访问 Redisson Map 缓存
        RMapCache<String, Object> rMapCache = redisson.getMapCache(RMAPCACHE_KEY);
        String value = LocalDateTime.now().toString();

        // 原子性操作：若 Key 已存在，则抛出幂等性异常
        Object existingValue = rMapCache.put(key, value, expireTime, timeUnit);
        if (Objects.nonNull(existingValue)) {
            throw new IdempotentException(info);
        }

        // 日志记录：幂等性 Key 成功存储
        LOGGER.info("[idempotent]: stored key={}, value={}, expireTime={} {}, now={}",
                key, value, expireTime, timeUnit, LocalDateTime.now());

        // 将幂等性信息保存到线程缓存中
        Map<String, Object> map = THREAD_CACHE.get();
        map.put(KEY, key);
        map.put(DELKEY, delKey);
    }

    /**
     * 方法执行后的拦截逻辑
     * - 根据配置决定是否移除缓存中的幂等性 Key。
     * - 清理当前线程的缓存数据。
     *
     * @param joinPoint 切点信息
     */
    @After("pointCut()")
    public void afterPointCut(JoinPoint joinPoint) {
        Map<String, Object> map = THREAD_CACHE.get();
        if (CollectionUtils.isEmpty(map)) {
            return;
        }

        RMapCache<Object, Object> mapCache = redisson.getMapCache(RMAPCACHE_KEY);
        if (mapCache.isEmpty()) {
            return;
        }

        String key = map.get(KEY).toString();
        boolean delKey = (boolean) map.get(DELKEY);

        // 如果配置了自动删除，则从缓存中移除 Key
        if (delKey) {
            mapCache.fastRemove(key);
            LOGGER.info("[idempotent]: removed key={}", key);
        }
        // 清理当前线程的缓存
        THREAD_CACHE.remove();
    }

    /**
     * 解析幂等性 Key 的方法
     * - 若注解中未配置 Key，则使用 URL 和参数列表生成默认 Key。
     * - 若注解中配置了 Key，则使用 KeyResolver 解析。
     *
     * @param idempotent Idempotent 注解
     * @param joinPoint 切点信息
     * @param request 当前请求对象
     * @return 解析后的 Key
     */
    private String resolveKey(Idempotent idempotent, JoinPoint joinPoint, HttpServletRequest request) {
        if (!StringUtils.hasLength(idempotent.key())) {
            // 默认使用 URL 和参数生成 Key
            String url = request.getRequestURI();
            String argString = Arrays.asList(joinPoint.getArgs()).toString();
            return url + argString;
        }
        // 使用自定义 KeyResolver 解析 Key
        return keyResolver.resolver(idempotent, joinPoint);
    }
}

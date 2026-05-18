package com.pig4cloud.pigx.common.audit.support;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig4cloud.pigx.common.audit.annotation.Audit;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 审计快照解析器。
 *
 * @author lengleng
 * @since 6.0
 */
@RequiredArgsConstructor
public class AuditValueResolver {

    private final ApplicationContext applicationContext;

    /**
     * 解析修改前快照。
     *
     * @param joinPoint 连接点
     * @param audit     审计注解
     * @return 修改前快照
     */
    public Object resolveOldVal(ProceedingJoinPoint joinPoint, Audit audit) {
        if (StringUtils.hasText(audit.id())) {
            return selectById(joinPoint, audit);
        }
        return SpelParser.parser(joinPoint, StringUtils.hasText(audit.oldVal()) ? audit.oldVal() : audit.spel());
    }

    /**
     * 解析修改后快照。
     *
     * @param joinPoint 连接点
     * @param audit     审计注解
     * @return 修改后快照
     */
    public Object resolveNewVal(ProceedingJoinPoint joinPoint, Audit audit) {
        if (StringUtils.hasText(audit.id())) {
            return selectById(joinPoint, audit);
        }
        return SpelParser.parser(joinPoint, StringUtils.hasText(audit.newVal()) ? audit.newVal() : audit.spel());
    }

    private Object selectById(ProceedingJoinPoint joinPoint, Audit audit) {
        Object id = SpelParser.parser(joinPoint, audit.id());
        if (Objects.isNull(id)) {
            return null;
        }
        if (!(id instanceof Serializable serializableId)) {
            throw new IllegalArgumentException("@Audit id 表达式解析结果必须实现 Serializable");
        }
        return resolveMapper(joinPoint, audit).selectById(serializableId);
    }

    private BaseMapper<?> resolveMapper(ProceedingJoinPoint joinPoint, Audit audit) {
        Object mapper;
        if (audit.mapper() == Void.class) {
            mapper = getBaseMapper(joinPoint);
        } else {
            try {
                mapper = applicationContext.getBean(audit.mapper());
            } catch (NoSuchBeanDefinitionException ex) {
                throw new IllegalStateException(
                        "未找到 @Audit mapper Bean: " + audit.mapper().getName() + "，请确认其已注册到 MyBatis-Plus", ex);
            }
        }
        if (mapper instanceof BaseMapper<?> baseMapper) {
            return baseMapper;
        }
        throw new IllegalStateException("@Audit mapper 必须是 MyBatis-Plus BaseMapper 类型");
    }

    private Object getBaseMapper(ProceedingJoinPoint joinPoint) {
        Object target = Objects.nonNull(joinPoint.getTarget()) ? joinPoint.getTarget() : joinPoint.getThis();
        if (Objects.isNull(target)) {
            throw new IllegalStateException("@Audit 未指定 mapper，且无法获取当前 Service 实例");
        }
        Method getBaseMapper = ReflectionUtils.findMethod(target.getClass(), "getBaseMapper");
        if (Objects.isNull(getBaseMapper)) {
            throw new IllegalStateException("@Audit 未指定 mapper，当前 Service 未提供 getBaseMapper()");
        }
        ReflectionUtils.makeAccessible(getBaseMapper);
        return ReflectionUtils.invokeMethod(getBaseMapper, target);
    }

}

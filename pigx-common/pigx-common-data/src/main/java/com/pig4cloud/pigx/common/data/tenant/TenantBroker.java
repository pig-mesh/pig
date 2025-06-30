package com.pig4cloud.pigx.common.data.tenant;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

/**
 * 租户运行时代理<br/>
 * 这是一个工具类，用于切换租户运行时，保护租户ID上下文<br/>
 * 下面这段代码演示问题所在 <pre>
 *     void methodA(){
 *         // 因为某些特殊原因，需要手动指定租户
 *         TenantContextHolder.setTenantId(1);
 *         // do something ...
 *     }
 *     void methodB(){
 *         // 因为某些特殊原因，需要手动指定租户
 *         TenantContextHolder.setTenantId(2);
 *         methodA();
 *         // 此时租户ID已经变成 1
 *         // do something ...
 *     }
 * </pre> 嵌套设置租户ID会导致租户上下文难以维护,并且很难察觉，容易导致数据错乱。 推荐的写法： <pre>
 *     void methodA(){
 *         TenantBroker.RunAs(1,() -> {
 *             // do something ...
 *         });
 *     }
 *     void methodB(){
 *         TenantBroker.RunAs(2,() -> {
 *              methodA();
 *             // do something ...
 *         });
 *     }
 * </pre>
 *
 * @author CJ (jclazz@outlook.com)
 * @date 2020/6/12
 * @since 3.9
 */
@Slf4j
@UtilityClass
public class TenantBroker {

    /**
     * 以指定租户身份执行操作
     *
     * @param tenant 租户ID
     * @param func   需要执行的操作
     * @throws TenantBrokerExceptionWrapper 执行过程中发生异常时抛出
     */
    public void runAs(Long tenant, RunAs<Long> func) {
        final Long pre = TenantContextHolder.getTenantId();
        try {
            log.trace("TenantBroker 切换租户{} -> {}", pre, tenant);
            TenantContextHolder.setTenantId(tenant);
            func.run(tenant);
        } catch (Exception e) {
            throw new TenantBrokerExceptionWrapper(e.getMessage(), e);
        } finally {
            log.trace("TenantBroker 还原租户{} <- {}", pre, tenant);
            TenantContextHolder.setTenantId(pre);
        }
    }

    /**
     * 以指定租户身份执行操作
     *
     * @param tenant 租户ID
     * @param func   需要执行的操作函数
     * @param <T>    操作返回的数据类型
     * @return 操作函数的执行结果
     * @throws TenantBrokerExceptionWrapper 执行过程中发生异常时抛出
     */
    public <T> T applyAs(Long tenant, ApplyAs<Long, T> func) {
        final Long pre = TenantContextHolder.getTenantId();
        try {
            log.trace("TenantBroker 切换租户{} -> {}", pre, tenant);
            TenantContextHolder.setTenantId(tenant);
            return func.apply(tenant);
        } catch (Exception e) {
            throw new TenantBrokerExceptionWrapper(e.getMessage(), e);
        } finally {
            log.trace("TenantBroker 还原租户{} <- {}", pre, tenant);
            TenantContextHolder.setTenantId(pre);
        }
    }

    /**
     * 以指定租户的身份执行操作
     *
     * @param supplier 租户ID提供者
     * @param func     要执行的操作
     */
    public void runAs(Supplier<Long> supplier, RunAs<Long> func) {
        runAs(supplier.get(), func);
    }

    /**
     * 执行给定的NoneAs操作，临时跳过租户上下文
     *
     * @param noneAs 要执行的操作
     * @param <T>    返回值的类型
     * @return 操作执行结果
     * @throws TenantBrokerExceptionWrapper 执行过程中\发生异常时抛出
     */
    public <T> T noneAs(NoneAs<T> noneAs) {
        try {
            TenantContextHolder.setTenantSkip();
            return noneAs.run();
        } catch (Exception e) {
            throw new TenantBrokerExceptionWrapper(e.getMessage(), e);
        } finally {
            TenantContextHolder.unsetTenantSkip();
        }
    }

    /**
     * 以指定租户的身份执行操作
     *
     * @param supplier 租户ID提供者
     * @param func     要执行的操作函数
     * @param <T>      返回数据的类型
     * @return 操作函数的执行结果
     */
    public <T> T applyAs(Supplier<Long> supplier, ApplyAs<Long, T> func) {
        return applyAs(supplier.get(), func);
    }

    @FunctionalInterface
    public interface RunAs<T> {

        /**
         * 执行业务逻辑
         *
         * @param tenantId 租户ID
         * @throws Exception 执行过程中可能抛出的异常
         */
        void run(T tenantId) throws Exception;

    }


    @FunctionalInterface
    public interface ApplyAs<T, R> {

        /**
         * 执行业务逻辑,返回一个值
         *
         * @param tenantId
         * @return
         * @throws Exception
         */
        R apply(T tenantId) throws Exception;

    }

    /**
     * 无参数函数式接口：用于定义无参数但有返回值的业务逻辑执行方法
     *
     * @author lengleng
     * @date 2025/06/27
     */
    @FunctionalInterface
    public interface NoneAs<R> {

        /**
         * 执行业务逻辑
         *
         * @return 业务执行结果
         * @throws Exception 执行过程中发生异常时抛出
         */
        R run() throws Exception;

    }

    public static class TenantBrokerExceptionWrapper extends RuntimeException {

        public TenantBrokerExceptionWrapper(String message, Throwable cause) {
            super(message, cause);
        }

        public TenantBrokerExceptionWrapper(Throwable cause) {
            super(cause);
        }

    }

}

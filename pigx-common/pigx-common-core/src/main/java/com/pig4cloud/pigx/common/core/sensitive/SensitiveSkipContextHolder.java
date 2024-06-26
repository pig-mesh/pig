package com.pig4cloud.pigx.common.core.sensitive;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.experimental.UtilityClass;

import java.util.Objects;

/**
 * 脱密跳过ttl
 *
 * @author lengleng
 * @date 2024/6/26
 */
@UtilityClass
public class SensitiveSkipContextHolder {

    private final ThreadLocal<Boolean> THREAD_LOCAL_SKIP = new TransmittableThreadLocal<>();

    /**
     * 设置是否跳过脱敏
     *
     * @param skip 是否跳过
     */
    public void setSkip(Boolean skip) {
        THREAD_LOCAL_SKIP.set(skip);
    }

    /**
     * 获取是否跳过脱敏
     *
     * @return 是否跳过
     */
    public Boolean getSkip() {
        return Objects.isNull(THREAD_LOCAL_SKIP.get()) ? Boolean.FALSE : THREAD_LOCAL_SKIP.get();
    }

    /**
     * 清除
     */
    public void clear() {
        THREAD_LOCAL_SKIP.remove();
    }
}

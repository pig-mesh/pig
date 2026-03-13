package com.pig4cloud.pig.common.sensitive.core;

import com.pig4cloud.pig.common.sensitive.annotation.Sensitive;

/**
 * 脱敏逻辑增强处理
 *
 * @author lengleng
 * @date 2024/6/27
 */
public interface SensitiveService {
    /**
     * 是否脱敏
     */
    boolean isSensitive(Sensitive sensitive);
}

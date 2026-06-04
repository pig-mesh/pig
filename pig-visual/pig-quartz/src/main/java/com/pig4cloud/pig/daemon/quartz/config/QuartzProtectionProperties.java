package com.pig4cloud.pig.daemon.quartz.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Quartz 重复触发保护配置。
 * <p>
 * 该配置统一控制 Quartz 任务的两层保护能力： 第一层用于拦截同一调度时间点的重复触发，第二层用于限制任务执行期间的并发进入。
 * </p>
 */
@Data
@ConfigurationProperties(prefix = "pig.quartz.protection")
public class QuartzProtectionProperties {

	/**
	 * 是否启用保护
	 */
	private boolean enabled = true;

	/**
	 * 同一触发点去重键有效期
	 */
	private long fireDedupTtlSeconds = 24 * 60 * 60L;

	/**
	 * 任务运行锁有效期
	 */
	private long runningLockTtlSeconds = 10 * 60L;

	/**
	 * 是否记录跳过日志
	 */
	private boolean logSkipped = true;

}

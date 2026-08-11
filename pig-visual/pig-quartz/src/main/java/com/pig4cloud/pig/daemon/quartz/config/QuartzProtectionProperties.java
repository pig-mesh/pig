package com.pig4cloud.pig.daemon.quartz.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashSet;
import java.util.Set;

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

	/**
	 * REST 任务允许访问的 URL 源白名单。
	 * <p>
	 * 白名单项格式为 {@code scheme://host[:port]}，例如
	 * {@code https://api.example.com}。匹配时仅比较协议、主机和有效端口， 任务地址可以携带具体的路径和查询参数。空白名单默认拒绝所有
	 * REST 任务请求。
	 * </p>
	 */
	private Set<String> restTaskUrlWhitelist = new HashSet<>();

}

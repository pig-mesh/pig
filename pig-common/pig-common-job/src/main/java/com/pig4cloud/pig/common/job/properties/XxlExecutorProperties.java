package com.pig4cloud.pig.common.job.properties;

import lombok.Data;

/**
 * xxl-job执行器配置
 *
 * @author lishangbu
 * @date 2020/9/14
 */
@Data
public class XxlExecutorProperties {

	/**
	 * 执行器AppName [选填]：执行器心跳注册分组依据；为空则关闭自动注册
	 */
	private String appname;

	/**
	 * 服务注册地址,优先使用该配置作为注册地址 为空时使用内嵌服务 ”IP:PORT“ 作为注册地址 从而更灵活的支持容器类型执行器动态IP和动态映射端口问题
	 */
	private String address;

	/**
	 * 执行器IP [选填]：默认为空表示自动获取IP，多网卡时可手动设置指定IP ，该IP不会绑定Host仅作为通讯实用；地址信息用于 "执行器注册" 和
	 * "调度中心请求并触发任务"
	 */
	private String ip;

	/**
	 * 执行器端口号 [选填]：小于等于0则自动获取；默认端口为9099，单机部署多个执行器时，注意要配置不同执行器端口；
	 */
	private Integer port = 9099;

	/**
	 * 执行器通讯TOKEN [选填]：非空时启用；
	 */
	private String accessToken;

	/**
	 * 执行器运行日志文件存储磁盘路径 [选填] ：需要对该路径拥有读写权限；为空则使用默认路径；
	 */
	private String logPath = "logs/applogs/xxl-job/jobhandler";

	/**
	 * 执行器日志保存天数 [选填] ：值大于3时生效，启用执行器Log文件定期清理功能，否则不生效；
	 */
	private Integer logRetentionDays = 30;

}

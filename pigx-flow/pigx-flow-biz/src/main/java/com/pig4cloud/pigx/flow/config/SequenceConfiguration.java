package com.pig4cloud.pigx.flow.config;

import cn.hutool.core.date.DateUtil;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import com.pig4cloud.pigx.common.sequence.builder.DbSeqBuilder;
import com.pig4cloud.pigx.common.sequence.properties.BaseSequenceProperties;
import com.pig4cloud.pigx.common.sequence.sequence.Sequence;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * 流程序列号生成器配置类
 * <p>
 * 配置流程模块专用的序列号生成器，用于生成各种业务编号。 支持多租户隔离，每个租户的序列号独立生成。 序列号格式：flow_租户ID_日期_序号，保证全局唯一性。 主要用于：
 * - 流程实例编号生成 - 任务编号生成 - 其他需要唯一标识的业务场景
 * </p>
 * 
 * @author lengleng
 * @date 2023-07-19
 */
@Configuration
public class SequenceConfiguration {

	/**
	 * 创建流程模块专用的序列号生成器
	 * <p>
	 * 基于数据库实现的分布式序列号生成器，特点： - 支持分布式环境下的并发生成 - 按租户和日期分段，避免序号过长 - 支持配置步长，提高生成性能 -
	 * 失败重试机制，保证可靠性
	 * </p>
	 * @param dataSource 数据源，用于访问序列号表
	 * @param properties 序列号配置属性，包含步长、重试次数等
	 * @return 配置好的序列号生成器实例
	 */
	@Bean
	public Sequence flowSequence(DataSource dataSource, BaseSequenceProperties properties) {
		return DbSeqBuilder.create()
			.bizName(() -> String.format("flow_%s_%s", TenantContextHolder.getTenantId(), DateUtil.today()))
			.dataSource(dataSource)
			.step(properties.getStep())
			.retryTimes(properties.getDb().getRetryTimes())
			.tableName(properties.getDb().getTableName())
			.build();
	}

	@Bean
	public EngineConfigurationConfigurer<SpringProcessEngineConfiguration> configurer() {
		return engineConfiguration -> {
			engineConfiguration.setEnableDatabaseEventLogging(true);
			engineConfiguration.setEnableVerboseExecutionTreeLogging(true);
		};
	}

}

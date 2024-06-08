package com.pig4cloud.pigx.flow.task.config;

import cn.hutool.core.date.DateUtil;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import com.pig4cloud.pigx.common.sequence.builder.DbSeqBuilder;
import com.pig4cloud.pigx.common.sequence.properties.BaseSequenceProperties;
import com.pig4cloud.pigx.common.sequence.sequence.Sequence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author lengleng
 * @date 2023-07-19
 * <p>
 * 设置发号器生成规则
 */
@Configuration
public class SequenceConfiguration {

	/**
	 * 工作流发号器
	 * @param dataSource
	 * @param properties
	 * @return
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

}

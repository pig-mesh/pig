package com.pig4cloud.pigx.common.sequence.properties;

/**
 * @author lengleng
 * @date 2019-05-26
 */

import com.baomidou.mybatisplus.annotation.DbType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author lengleng
 * @date 2019/5/26
 * <p>
 * 发号器DB配置属性
 */
@Data
@Component
@ConfigurationProperties(prefix = "pigx.xsequence.db")
public class SequenceDbProperties extends BaseSequenceProperties {

	/**
	 * 默认数据库类型
	 */
	private DbType dbType = DbType.MYSQL;

	/**
	 * 表名称
	 */
	private String tableName = "pigx_sequence";

	/**
	 * 重试次数
	 */
	private int retryTimes = 1;

}

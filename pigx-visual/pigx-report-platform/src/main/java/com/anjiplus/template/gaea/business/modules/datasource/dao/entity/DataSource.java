
package com.anjiplus.template.gaea.business.modules.datasource.dao.entity;

import com.anji.plus.gaea.annotation.Unique;
import com.anji.plus.gaea.curd.entity.GaeaBaseEntity;
import com.anjiplus.template.gaea.business.code.ResponseCode;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description 数据源 entity
 * @author Raod
 * @date 2021-03-18 12:09:57.728203200
 **/
@TableName(keepGlobalPrefix = true, value = "gaea_report_data_source")
@Data
public class DataSource extends GaeaBaseEntity {

	@Schema(description = "数据源编码")
	@Unique(code = ResponseCode.SOURCE_CODE_ISEXIST)
	private String sourceCode;

	@Schema(description = "数据源名称")
	private String sourceName;

	@Schema(description = "数据源描述")
	private String sourceDesc;

	@Schema(
			description = "数据源类型 DIC_NAME=SOURCE_TYPE; mysql，orace，sqlserver，elasticsearch，接口，javaBean，数据源类型字典中item-extend动态生成表单")
	private String sourceType;

	@Schema(
			description = "数据源连接配置json：关系库{ jdbcUrl:'', username:'', password:'','driverName':''}ES-sql{ apiUrl:'http://127.0.0.1:9092/_xpack/sql?format=json','method':'POST','body':'{\"query\":\"select 1\"}' }  接口{ apiUrl:'http://ip:port/url', method:'' } javaBean{ beanNamw:'xxx' }")
	private String sourceConfig;

	@Schema(description = "0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG")
	private Integer enableFlag;

	@Schema(description = "0--未删除 1--已删除 DIC_NAME=DELETE_FLAG")
	private Integer deleteFlag;

}

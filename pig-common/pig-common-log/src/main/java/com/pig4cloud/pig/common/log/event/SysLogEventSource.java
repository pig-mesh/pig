package com.pig4cloud.pig.common.log.event;

import java.io.Serial;

import com.pig4cloud.pig.admin.api.entity.SysLog;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * spring event log
 *
 * @author lengleng
 * @date 2023/8/11
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysLogEventSource extends SysLog {

	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 参数重写成object
	 */
	private Object body;

}

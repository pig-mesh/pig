package com.pig4cloud.pig.common.log.event;

import java.io.Serial;

import com.pig4cloud.pig.admin.api.entity.SysLog;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统日志事件源类，继承自SysLog
 *
 * @author lengleng
 * @date 2025/05/31
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

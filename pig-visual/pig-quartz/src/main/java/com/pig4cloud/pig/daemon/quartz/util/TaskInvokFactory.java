package com.pig4cloud.pig.daemon.quartz.util;

import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pig.common.core.util.SpringContextHolder;
import com.pig4cloud.pig.daemon.quartz.constants.JobTypeQuartzEnum;
import com.pig4cloud.pig.daemon.quartz.exception.TaskException;
import lombok.extern.slf4j.Slf4j;

/**
 * 任务调用工厂类：根据任务类型获取对应的任务调用器
 *
 * @author lengleng
 * @version 1.0
 * @date 2025/05/31
 */
@Slf4j
public class TaskInvokFactory {

	/**
	 * 根据任务类型获取对应的任务执行器
	 * @param jobType 任务类型
	 * @return 任务执行器实例
	 * @throws TaskException 当任务类型为空或不支持时抛出异常
	 */
	public static ITaskInvok getInvoker(String jobType) throws TaskException {
		if (StrUtil.isBlank(jobType)) {
			log.info("获取TaskInvok传递参数有误，jobType:{}", jobType);
			throw new TaskException("");
		}

		ITaskInvok iTaskInvok = null;
		if (JobTypeQuartzEnum.JAVA.getType().equals(jobType)) {
			iTaskInvok = SpringContextHolder.getBean("javaClassTaskInvok");
		}
		else if (JobTypeQuartzEnum.SPRING_BEAN.getType().equals(jobType)) {
			iTaskInvok = SpringContextHolder.getBean("springBeanTaskInvok");
		}
		else if (JobTypeQuartzEnum.REST.getType().equals(jobType)) {
			iTaskInvok = SpringContextHolder.getBean("restTaskInvok");
		}
		else if (JobTypeQuartzEnum.JAR.getType().equals(jobType)) {
			iTaskInvok = SpringContextHolder.getBean("jarTaskInvok");
		}
		else if (StrUtil.isBlank(jobType)) {
			log.info("定时任务类型无对应反射方式，反射类型:{}", jobType);
			throw new TaskException("");
		}

		return iTaskInvok;
	}

}

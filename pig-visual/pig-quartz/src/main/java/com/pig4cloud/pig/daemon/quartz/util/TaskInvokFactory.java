package com.pig4cloud.pig.daemon.quartz.util;

import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pig.common.core.util.SpringContextHolder;
import com.pig4cloud.pig.daemon.quartz.constants.JobTypeQuartzEnum;
import com.pig4cloud.pig.daemon.quartz.exception.TaskException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/8/8 15:40 TaskInvok工厂类
 */
@Slf4j
public class TaskInvokFactory {

	/**
	 * 根据对应jobType获取对应 invoker
	 * @param jobType
	 * @return
	 * @throws TaskException
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

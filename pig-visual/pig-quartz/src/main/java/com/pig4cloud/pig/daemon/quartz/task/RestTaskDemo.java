package com.pig4cloud.pig.daemon.quartz.task;

import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.security.annotation.Inner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * 用于测试REST风格调用的演示类
 *
 * @author lengleng
 * @date 2025/05/31
 */
@Slf4j
@RestController
@RequestMapping("/inner-job")
public class RestTaskDemo {

	/**
	 * REST风格调用定时任务的演示方法
	 * @param param 路径参数
	 * @return 统一响应结果
	 */
	@Inner(value = false)
	@GetMapping("/{param}")
	public R demoMethod(@PathVariable("param") String param) {
		log.info("测试于:{}，传入参数{}", LocalDateTime.now(), param);
		return R.ok();
	}

}

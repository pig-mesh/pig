package com.pig4cloud.pig.daemon.quartz.task;

import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.security.annotation.Inner;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(description = "REST风格", name = "REST风格调用管理模块")
public class RestTaskDemo {

	/**
	 * REST风格调用定时任务的演示方法
	 * @param param 路径参数
	 * @return 统一响应结果
	 */
	@Inner(value = false)
	@GetMapping("/{param}")
	@Operation(summary = "REST风格调用定时任务的演示方法", description = "REST风格调用定时任务的演示方法")
	public R demoMethod(@PathVariable("param") String param) {
		log.info("测试于:{}，传入参数{}", LocalDateTime.now(), param);
		return R.ok();
	}

}

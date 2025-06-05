package com.pig4cloud.pig.admin.controller;

import com.pig4cloud.pig.common.core.util.R;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 系统监控控制器：提供系统监控相关接口
 *
 * @author lengleng
 * @date 2025/05/30
 */
@RestController
@RequestMapping("/system")
@RequiredArgsConstructor
@Tag(description = "system", name = "系统监控")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysSystemInfoController {

	private final RedisTemplate<String, String> redisTemplate;

	/**
	 * 获取Redis缓存监控信息
	 * @return 包含Redis信息、数据库大小和命令统计的响应结果
	 */
	@GetMapping("/cache")
	public R cache() {
		Properties info = (Properties) redisTemplate.execute((RedisCallback<Object>) RedisServerCommands::info);
		Properties commandStats = (Properties) redisTemplate
			.execute((RedisCallback<Object>) connection -> connection.serverCommands().info("commandstats"));
		Object dbSize = redisTemplate.execute((RedisCallback<Object>) RedisServerCommands::dbSize);

		if (commandStats == null) {
			return R.failed("获取异常");
		}

		Map<String, Object> result = new HashMap<>(3);
		result.put("info", info);
		result.put("dbSize", dbSize);

		List<Map<String, String>> pieList = new ArrayList<>();
		commandStats.stringPropertyNames().forEach(key -> {
			Map<String, String> data = new HashMap<>(2);
			String property = commandStats.getProperty(key);
			data.put("name", StringUtils.removeStart(key, "cmdstat_"));
			data.put("value", StringUtils.substringBetween(property, "calls=", ",usec"));
			pieList.add(data);
		});

		result.put("commandStats", pieList);
		return R.ok(result);
	}

}

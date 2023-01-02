package com.pig4cloud.pigx.codegen.controller;

import com.pig4cloud.pigx.codegen.entity.GenConfig;
import com.pig4cloud.pigx.codegen.service.GenDynamicService;
import com.pig4cloud.pigx.codegen.util.SqlDto;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author lengleng
 * @date 2022/7/9
 *
 * 动态数据 无代码
 */
@RestController
@Inner(value = false)
@RequiredArgsConstructor
@RequestMapping("/dynamic")
@Tag(description  = "dynamic", name =  "无代码管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class GenDynamicController {

	private final GenDynamicService dynamicService;

	@GetMapping("/gen")
	public R genCode(GenConfig genConfig) {
		return R.ok(dynamicService.run(genConfig));
	}

	@PostMapping("/dynamic-query")
	public R dynamicQuery(@RequestBody SqlDto sqlDto) {
		List<LinkedHashMap<String, Object>> linkedHashMaps = dynamicService.dynamicQuery(sqlDto);
		return R.ok(linkedHashMaps);
	}

}

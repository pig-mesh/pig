/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */
package com.pig4cloud.pig.codegen.controller;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.smallbun.screw.boot.config.Screw;
import cn.smallbun.screw.boot.properties.ScrewProperties;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.codegen.entity.GenDatasourceConf;
import com.pig4cloud.pig.codegen.service.GenDatasourceConfService;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.core.util.SpringContextHolder;
import com.pig4cloud.pig.common.security.annotation.Inner;
import com.pig4cloud.pig.common.xss.core.XssCleanIgnore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;

/**
 * 数据源管理控制器
 *
 * @author lengleng
 * @date 2025/05/31
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/dsconf")
@Tag(description = "dsconf", name = "数据源管理模块")
public class GenDsConfController {

	private final GenDatasourceConfService datasourceConfService;

	private final Screw screw;

	/**
	 * 分页查询数据源配置
	 * @param page 分页参数对象
	 * @param datasourceConf 数据源配置查询条件
	 * @return 分页查询结果
	 */
	@GetMapping("/page")
	@Operation(summary = "分页查询数据源配置", description = "分页查询数据源配置")
	public R getDsConfPage(Page page, GenDatasourceConf datasourceConf) {
		return R.ok(datasourceConfService.page(page,
				Wrappers.<GenDatasourceConf>lambdaQuery()
					.like(StrUtil.isNotBlank(datasourceConf.getDsName()), GenDatasourceConf::getDsName,
							datasourceConf.getDsName())));
	}

	/**
	 * 查询全部数据源列表
	 * @return 包含全部数据源列表的响应结果
	 */
	@Inner(value = false)
	@GetMapping("/list")
	@Operation(summary = "查询全部数据源列表", description = "查询全部数据源列表")
	public R listDsConfs() {
		return R.ok(datasourceConfService.list());
	}

	/**
	 * 根据ID查询数据源表
	 * @param id 数据源ID
	 * @return 包含查询结果的响应对象
	 */
	@GetMapping("/{id}")
	@Operation(summary = "根据ID查询数据源表", description = "根据ID查询数据源表")
	public R getDsConfById(@PathVariable("id") Long id) {
		return R.ok(datasourceConfService.getById(id));
	}

	/**
	 * 新增数据源表
	 * @param datasourceConf 数据源配置信息
	 * @return 操作结果
	 */
	@PostMapping
	@XssCleanIgnore
	@Operation(summary = "新增数据源表", description = "新增数据源表")
	public R saveDsConf(@RequestBody GenDatasourceConf datasourceConf) {
		return R.ok(datasourceConfService.saveDsByEnc(datasourceConf));
	}

	/**
	 * 修改数据源表
	 * @param conf 数据源表配置信息
	 * @return 操作结果
	 */
	@PutMapping
	@XssCleanIgnore
	@Operation(summary = "修改数据源表", description = "修改数据源表")
	public R updateDsConf(@RequestBody GenDatasourceConf conf) {
		return R.ok(datasourceConfService.updateDsByEnc(conf));
	}

	/**
	 * 通过id数组删除数据源表
	 * @param ids 要删除的数据源id数组
	 * @return 包含操作结果的R对象
	 */
	@DeleteMapping
	@Operation(summary = "通过id数组删除数据源表", description = "通过id数组删除数据源表")
	public R removeDsConfByIds(@RequestBody Long[] ids) {
		return R.ok(datasourceConfService.removeByDsId(ids));
	}

	/**
	 * 生成指定数据源的数据库文档并输出到响应流
	 * @param dsName 数据源名称
	 * @param response HTTP响应对象
	 * @throws Exception 生成文档或IO操作过程中可能抛出的异常
	 */
	@SneakyThrows
	@GetMapping("/doc")
	@Operation(summary = "生成指定数据源的数据库文档并输出到响应流", description = "生成指定数据源的数据库文档并输出到响应流")
	public void generatorDoc(String dsName, HttpServletResponse response) {
		// 设置指定的数据源
		DynamicRoutingDataSource dynamicRoutingDataSource = SpringContextHolder.getBean(DynamicRoutingDataSource.class);
		DynamicDataSourceContextHolder.push(dsName);
		DataSource dataSource = dynamicRoutingDataSource.determineDataSource();

		// 设置指定的目标表
		ScrewProperties screwProperties = SpringContextHolder.getBean(ScrewProperties.class);

		// 生成
		byte[] data = screw.documentGeneration(dsName, dataSource, screwProperties).toByteArray();
		response.reset();
		response.addHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(data.length));
		response.setContentType("application/octet-stream");
		IoUtil.write(response.getOutputStream(), Boolean.FALSE, data);
	}

}

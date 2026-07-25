/*
 *    Copyright (c) 2018-2026, lengleng All rights reserved.
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

package com.pig4cloud.pig.admin.service.impl;

import com.pig4cloud.pig.admin.mapper.SysLogMapper;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SysLogServiceImplTests {

	@Test
	void getLogSumUsesDatabaseAggregationAndKeepsResponseShape() {
		SysLogMapper sysLogMapper = mock(SysLogMapper.class);
		SysLogServiceImpl service = new SysLogServiceImpl();
		ReflectionTestUtils.setField(service, "baseMapper", sysLogMapper);
		when(sysLogMapper.selectLogSum(any()))
			.thenReturn(List.of(row("2026-07-13", "0", 12L), row("2026-07-13", "9", 3L), row("2026-07-14", "0", 8L)));
		when(sysLogMapper.selectList(any())).thenThrow(new AssertionError("不应加载日志实体进行内存聚合"));

		List<Map<String, Object>> result = service.getLogSum();

		assertThat(result).containsExactly(Map.of("createTime", "2026-07-13", "0", 12, "9", 3),
				Map.of("createTime", "2026-07-14", "0", 8));
		verify(sysLogMapper).selectLogSum(any());
	}

	@Test
	void getLogSumNormalizesTimestampBucketsFromOracle() {
		SysLogMapper sysLogMapper = mock(SysLogMapper.class);
		SysLogServiceImpl service = new SysLogServiceImpl();
		ReflectionTestUtils.setField(service, "baseMapper", sysLogMapper);
		when(sysLogMapper.selectLogSum(any()))
			.thenReturn(List.of(row(Timestamp.valueOf("2026-07-13 00:00:00"), "0", 12L)));

		List<Map<String, Object>> result = service.getLogSum();

		assertThat(result).containsExactly(Map.of("createTime", "2026-07-13", "0", 12));
	}

	private Map<String, Object> row(Object createTime, String logType, long logCount) {
		Map<String, Object> row = new LinkedHashMap<>();
		row.put("createTime", createTime);
		row.put("logType", logType);
		row.put("logCount", logCount);
		return row;
	}

}

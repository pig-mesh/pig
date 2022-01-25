package com.pig4cloud.pigx.codegen.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.codegen.entity.ColumnEntity;
import com.pig4cloud.pigx.codegen.entity.GenConfig;
import com.pig4cloud.pigx.codegen.mapper.GeneratorMapper;
import com.pig4cloud.pigx.codegen.service.GenTableColumnService;
import com.pig4cloud.pigx.codegen.util.GenUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 表字段信息管理
 *
 * @author lengleng
 * @date 2020/5/18
 */
@Service
@RequiredArgsConstructor
public class GenTableColumnServiceImpl implements GenTableColumnService {

	@Override
	public IPage<ColumnEntity> listTable(Page page, GenConfig genConfig) {
		GeneratorMapper mapper = GenUtils.getMapper(genConfig.getDsName());

		// 关闭sql 优化
		page.setOptimizeCountSql(false);
		IPage<ColumnEntity> columnPage = mapper.selectTableColumn(page, genConfig.getTableName(),
				genConfig.getDsName());

		// 处理 数据库类型和 Java 类型关系
		Configuration config = GenUtils.getConfig();
		columnPage.getRecords().forEach(column -> {
			// 只保留 （）之前部分，例如 timestamp(6) -> timestamp
			String dataType = StrUtil.subBefore(column.getDataType(), "(", false);
			String attrType = config.getString(dataType, "unknowType");
			column.setLowerAttrName(StringUtils.uncapitalize(GenUtils.columnToJava(column.getColumnName())));
			column.setJavaType(attrType);
		});
		return columnPage;
	}

}

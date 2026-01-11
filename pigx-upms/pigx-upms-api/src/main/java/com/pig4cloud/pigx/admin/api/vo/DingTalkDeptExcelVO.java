package com.pig4cloud.pigx.admin.api.vo;

import cn.idev.excel.annotation.ExcelIgnore;
import cn.idev.excel.annotation.ExcelProperty;
import com.pig4cloud.pigx.common.excel.annotation.ExcelLine;
import lombok.Data;

import java.io.Serializable;

/**
 * 钉钉部门导入VO
 * 用于解析钉钉导出的Excel中的部门数据
 *
 * @author lengleng
 * @date 2026-01-10
 */
@Data
public class DingTalkDeptExcelVO implements Serializable {

	/**
	 * 导入时回显行号(用于错误定位)
	 */
	@ExcelLine
	@ExcelIgnore
	private Long lineNum;

	/**
	 * 部门路径
	 * 格式: "公司/部门A/子部门B" (用"/"表示层级关系)
	 * 同一行可能包含多个部门路径,用";"分隔
	 */
	@ExcelProperty("部门")
	private String deptPath;

}

package com.pig4cloud.pigx.admin.api.vo;

import org.apache.fesod.sheet.annotation.ExcelIgnore;
import org.apache.fesod.sheet.annotation.ExcelProperty;
import com.pig4cloud.pigx.common.excel.annotation.ExcelLine;
import lombok.Data;

import java.io.Serializable;

/**
 * 企业微信部门导入VO
 * 用于解析企业微信导出的通讯录Excel中的部门数据
 *
 * @author lengleng
 * @date 2026-01-10
 */
@Data
public class WeChatDeptExcelVO implements Serializable {

	/**
	 * 导入时回显行号(用于错误定位)
	 */
	@ExcelLine
	@ExcelIgnore
	private Long lineNum;

	/**
	 * 部门路径
	 * 格式: "腾讯公司/微信事业群/广州研发部" (用"/"表示层级关系)
	 * 同一行可能包含多个部门路径,用";"分隔,如: "部门路径1;部门路径2"
	 * 企业微信Excel中对应"部门"列(第5列,索引4)
	 */
	@ExcelProperty(value = "部门", index = 4)
	private String deptPath;

}

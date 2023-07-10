package com.pig4cloud.pigx.admin.api.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.pig4cloud.pigx.common.excel.annotation.ExcelLine;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class DingUserExcelVo implements Serializable {

	/**
	 * 导入时候回显行号
	 */
	@ExcelLine
	@ExcelIgnore
	private Long lineNum;

	/**
	 * 手机号
	 */
	@NotBlank(message = "手机号不能为空")
	@ExcelProperty("手机号")
	private String phone;

	/**
	 * 姓名
	 */
	@NotBlank(message = "姓名不能为空")
	@ExcelProperty("姓名")
	private String name;

	/**
	 * 部门名称
	 */
	@NotBlank(message = "部门不能为空")
	@ExcelProperty("部门")
	private String deptName;

	@ExcelProperty("邮箱")
	private String email;

	/**
	 * 锁定标记
	 */
	@ExcelProperty("激活状态,0:是,9:否")
	private String lockFlag;

}

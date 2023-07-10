package com.pig4cloud.pigx.admin.api.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.pig4cloud.pigx.common.excel.annotation.ExcelLine;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 企业微信用户导入
 */
@Data
public class CpUserExcelVo implements Serializable {

	/**
	 * 导入时候回显行号
	 */
	@ExcelLine
	@ExcelIgnore
	private Long lineNum;

	@NotBlank(message = "帐号不能为空")
	@ExcelProperty("帐号")
	private String username;

	/**
	 * 手机号
	 */
	@NotBlank(message = "手机号不能为空")
	@ExcelProperty("手机")
	private String phone;

	/**
	 * 姓名
	 */
	@NotBlank(message = "姓名不能为空")
	@ExcelProperty("姓名")
	private String name;

	/**
	 * 别名
	 */
	@ExcelProperty("别名")
	private String nickname;

	/**
	 * 部门名称
	 */
	@NotBlank(message = "部门不能为空")
	@ExcelProperty("部门")
	private String deptName;

	@ExcelProperty("企业邮箱")
	private String email;

	/**
	 * 锁定标记
	 */
	@ExcelProperty("激活状态,0:已激活,9:未激活")
	private String lockFlag;

}

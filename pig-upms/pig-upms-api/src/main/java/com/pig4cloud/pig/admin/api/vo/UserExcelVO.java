package com.pig4cloud.pig.admin.api.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.pig4cloud.plugin.excel.annotation.ExcelLine;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户excel 对应的实体
 *
 * @author lengleng
 * @date 2021/8/4
 */
@Data
@ColumnWidth(30)
public class UserExcelVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * excel 行号
	 */
	@ExcelLine
	@ExcelIgnore
	private Long lineNum;

	/**
	 * 主键ID
	 */
	@ExcelProperty("用户编号")
	private Long userId;

	/**
	 * 用户名
	 */
	@NotBlank(message = "用户名不能为空")
	@ExcelProperty("用户名")
	private String username;

	/**
	 * 手机号
	 */
	@NotBlank(message = "手机号不能为空")
	@ExcelProperty("手机号")
	private String phone;

	/**
	 * 部门名称
	 */
	@NotBlank(message = "部门名称不能为空")
	@ExcelProperty("部门名称")
	private String deptName;

	/**
	 * 角色列表
	 */
	@NotBlank(message = "角色不能为空")
	@ExcelProperty("角色")
	private String roleNameList;

	/**
	 * 岗位列表
	 */
	@NotBlank(message = "岗位不能为空")
	@ExcelProperty("岗位")
	private String postNameList;

	/**
	 * 锁定标记
	 */
	@ExcelProperty("锁定标记,0:正常,9:已锁定")
	private String lockFlag;

	/**
	 * 创建时间
	 */
	@ExcelProperty(value = "创建时间")
	private LocalDateTime createTime;

}

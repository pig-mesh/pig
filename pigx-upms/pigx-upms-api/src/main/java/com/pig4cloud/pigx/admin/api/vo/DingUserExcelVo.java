package com.pig4cloud.pigx.admin.api.vo;

import cn.idev.excel.annotation.ExcelIgnore;
import cn.idev.excel.annotation.ExcelProperty;
import com.pig4cloud.pigx.common.excel.annotation.ExcelLine;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * 钉钉用户Excel导入导出VO
 *
 * @author lengleng
 * @date 2025/06/30
 */
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
	 * 部门路径
	 * 格式: "公司/部门A/子部门B" (用"/"表示层级关系)
	 * 支持多部门: "部门1;部门2" (用英文分号分隔,第一个为主部门)
	 */
	@NotBlank(message = "部门不能为空")
	@ExcelProperty("部门")
	private String deptPath;

	/**
	 * 邮箱
	 */
	@ExcelProperty("邮箱")
	private String email;

	/**
	 * 锁定标记
	 * 0: 正常(激活)
	 * 9: 锁定(未激活)
	 */
	@ExcelProperty("激活状态,0:是,9:否")
	private String lockFlag;

}

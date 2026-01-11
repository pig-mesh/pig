package com.pig4cloud.pigx.admin.api.vo;

import cn.idev.excel.annotation.ExcelIgnore;
import cn.idev.excel.annotation.ExcelProperty;
import com.pig4cloud.pigx.common.excel.annotation.ExcelLine;
import lombok.Data;

import java.io.Serializable;

/**
 * 企业微信用户导入VO
 * 用于解析企业微信导出的通讯录Excel中的用户数据
 *
 * @author lengleng
 * @date 2026-01-10
 */
@Data
public class WeChatUserExcelVO implements Serializable {

	/**
	 * 导入时回显行号(用于错误定位)
	 */
	@ExcelLine
	@ExcelIgnore
	private Long lineNum;

	/**
	 * 姓名
	 */
	@ExcelProperty(value = "姓名", index = 0)
	private String name;

	/**
	 * 账号 (userid)
	 * 企业微信中的唯一标识
	 */
	@ExcelProperty(value = "账号", index = 1)
	private String account;

	/**
	 * 别名
	 */
	@ExcelProperty(value = "别名", index = 2)
	private String alias;

	/**
	 * 职务
	 */
	@ExcelProperty(value = "职务", index = 3)
	private String position;

	/**
	 * 部门路径
	 * 格式: "腾讯公司/微信事业群/广州研发部"
	 * 支持多部门: "部门1;部门2" (用英文分号分隔,第一个为主部门)
	 * 实际Excel列：第5列（索引4）
	 */
	@ExcelProperty(value = "部门", index = 4)
	private String deptPath;

	/**
	 * 性别 (男/女)
	 */
	@ExcelProperty(value = "性别", index = 5)
	private String gender;

	/**
	 * 手机号 (关键字段,用于判重)
	 */
	@ExcelProperty(value = "手机", index = 6)
	private String phone;

	/**
	 * 座机
	 */
	@ExcelProperty(value = "座机", index = 7)
	private String telephone;

	/**
	 * 邮箱
	 */
	@ExcelProperty(value = "邮箱", index = 8)
	private String email;

}

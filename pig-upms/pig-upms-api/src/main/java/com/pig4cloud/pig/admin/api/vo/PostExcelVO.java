package com.pig4cloud.pig.admin.api.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.pig4cloud.plugin.excel.annotation.ExcelLine;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 岗位excel 对应的实体
 *
 * @author fxz
 * @date 2022/3/21
 */
@Data
@ColumnWidth(30)
public class PostExcelVO implements Serializable {

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
	@ExcelProperty("岗位编号")
	private Long postId;

	/**
	 * 岗位名称
	 */
	@NotBlank(message = "岗位名称不能为空")
	@ExcelProperty("岗位名称")
	private String postName;

	/**
	 * 岗位标识
	 */
	@NotBlank(message = "岗位标识不能为空")
	@ExcelProperty("岗位标识")
	private String postCode;

	/**
	 * 岗位排序
	 */
	@NotNull(message = "岗位排序不能为空")
	@ExcelProperty("岗位排序")
	private Integer postSort;

	/**
	 * 岗位描述
	 */
	@NotBlank(message = "岗位描述不能为空")
	@ExcelProperty(value = "岗位描述")
	private String remark;

	/**
	 * 创建时间
	 */
	@ExcelProperty(value = "创建时间")
	private LocalDateTime createTime;

}

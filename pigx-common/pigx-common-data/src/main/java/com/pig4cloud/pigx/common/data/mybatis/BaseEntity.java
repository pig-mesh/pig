package com.pig4cloud.pigx.common.data.mybatis;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 抽象实体
 *
 * @author lengleng
 * @date 2021/8/9
 */
@Getter
@Setter
public class BaseEntity implements Serializable {

	/**
	 * 创建者
	 */
	@TableField(fill = FieldFill.INSERT)
	@ApiModelProperty(value = "创建人", example = "admin")
	@ExcelProperty(value = "创建人")
	private String createBy;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	@ApiModelProperty(value = "创建时间", example = "2021-12-20 13:27:44")
	@ExcelProperty(value = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 更新者
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@ApiModelProperty(value = "更新人", example = "admin")
	@ExcelIgnore
	private String updateBy;

	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@ApiModelProperty(value = "更新时间", example = "2021-12-20 13:27:44")
	@ExcelIgnore
	private LocalDateTime updateTime;

	/**
	 * 是否删除 1：已删除 0：正常
	 */
	@TableLogic
	@ApiModelProperty(value = "删除标记,1:已删除,0:正常")
	@TableField(fill = FieldFill.INSERT)
	@ExcelIgnore
	private String delFlag;

}

package com.pig4cloud.pigx.admin.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 文件类型
 *
 * @author lengleng
 * @date 2023/7/25
 */
@Data
@TenantTable
@Schema(description = "文件")
@EqualsAndHashCode(callSuper = true)
public class SysFileGroup extends Model<SysFile> {

	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.ASSIGN_ID)
	@Schema(description = "主键ID")
	private Long id;

	@Schema(description = "父级ID")
	private Long pid;

	@Schema(description = "分类类型: [10=图片,20=视频]")
	private Long type;

	@Schema(description = "分类名称")
	private String name;

	/**
	 * 上传人
	 */
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "创建者")
	private String createBy;

	/**
	 * 上传时间
	 */
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 更新人
	 */
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "更新者")
	private String updateBy;

	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.UPDATE)
	@Schema(description = "更新时间")
	private LocalDateTime updateTime;

	/**
	 * 删除标识：1-删除，0-正常
	 */
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "删除标记,1:已删除,0:正常")
	private String delFlag;

}

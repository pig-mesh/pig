package com.pig4cloud.pigx.app.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * App 底部导航实体。
 * <p>
 * 该表是移动端底部 Tabbar 的唯一数据源。图标字段保存 Wot UI 图标名，
 * 运行时可见性由 {@link #loginFlag} 和 {@code app_role_tabbar} 共同决定。
 *
 * @author lengleng
 * @date 2023-06-08 11:18:46
 */
@Data
@TenantTable
@TableName("app_tabbar")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "导航栏")
public class AppTabbarEntity extends Model<AppTabbarEntity> {

	/**
	 * 主键
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@Schema(description = "主键")
	private Long id;

	/**
	 * 导航名称
	 */
	@Schema(description = "导航名称")
	private String name;

	/**
     * 选中态图标名。
     * <p>
     * 保存 Wot UI 内置图标名称，例如 {@code home-fill}。
	 */
    @Schema(description = "选中态图标名")
	private String selected;

	/**
     * 未选中态图标名。
     * <p>
     * 保存 Wot UI 内置图标名称，例如 {@code home}。
	 */
    @Schema(description = "未选中态图标名")
	private String unselected;

	/**
     * 跳转链接 JSON。
     * <p>
     * 前端按装修链接结构解析，例如 {@code {"path":"/pages/index/index","type":"shop"}}。
	 */
	@Schema(description = "链接地址")
	@JsonRawValue
	@JsonDeserialize()
	private String link;

	/**
	 * 排序顺序
	 */
	@Schema(description = "排序值")
	private Integer sortOrder;

    /**
     * 是否需要登录，0=不需要登录也可见，1=登录后按角色授权可见。
     */
    @Schema(description = "是否需要登录，0=不需要登录也可见，1=登录后按角色授权可见")
    private String loginFlag;

	/**
	 * 创建人
	 */
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "创建人")
	private String createBy;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 更新人
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@Schema(description = "更新人")
	private String updateBy;

	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@Schema(description = "更新时间")
	private LocalDateTime updateTime;

	/**
	 * 删除标记
	 */
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "删除标记")
	private String delFlag;

}

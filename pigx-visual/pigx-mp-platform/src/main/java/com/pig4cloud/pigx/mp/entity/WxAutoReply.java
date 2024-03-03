package com.pig4cloud.pigx.mp.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 消息自动回复
 *
 * @author JL
 * @date 2019-04-18 15:40:39
 */
@Data
@TenantTable
@TableName("wx_auto_reply")
@EqualsAndHashCode(callSuper = true)
public class WxAutoReply extends Model<WxAutoReply> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long id;

	/**
	 * 公众号ID
	 */
	private String appId;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 类型（1、关注时回复；2、消息回复；3、关键词回复）
	 */
	@NotNull(message = "类型不能为空")
	private String type;

	/**
	 * 关键词
	 */
	private String reqKey;

	/**
	 * 请求消息类型（text：文本；image：图片；voice：语音；video：视频；shortvideo：小视频；location：地理位置）
	 */
	private String reqType;

	/**
	 * 回复消息类型（text：文本；image：图片；voice：语音；video：视频；music：音乐；news：图文）
	 */
	@NotNull(message = "回复消息类型不能为空")
	private String repType;

	/**
	 * 回复类型文本匹配类型（1、全匹配，2、半匹配）
	 */
	private String repMate;

	/**
	 * 回复类型文本保存文字
	 */
	private String repContent;

	/**
	 * 回复的素材名、视频和音乐的标题
	 */
	private String repName;

	/**
	 * 回复类型imge、voice、news、video的mediaID或音乐缩略图的媒体id
	 */
	private String repMediaId;

	/**
	 * 视频和音乐的描述
	 */
	private String repDesc;

	/**
	 * 链接
	 */
	private String repUrl;

	/**
	 * 高质量链接
	 */
	private String repHqUrl;

	/**
	 * 缩略图的媒体id
	 */
	private String repThumbMediaId;

	/**
	 * 缩略图url
	 */
	private String repThumbUrl;

	/**
	 * 图文消息的内容
	 */
	private String content;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime updateTime;

	/**
	 * 是否删除 -1：已删除 0：正常
	 */
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	private String delFlag;

}

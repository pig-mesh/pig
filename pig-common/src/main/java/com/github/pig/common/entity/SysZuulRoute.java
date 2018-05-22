package com.github.pig.common.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 动态路由配置表
 * </p>
 *
 * @author lengleng
 * @since 2018-05-15
 */
@Data
@TableName("sys_zuul_route")
public class SysZuulRoute extends Model<SysZuulRoute> {

    private static final long serialVersionUID = 1L;

    /**
     * router Id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 路由路径
     */
    private String path;
    /**
     * 服务名称
     */
    @TableField("service_id")
    private String serviceId;
    /**
     * url代理
     */
    private String url;
    /**
     * 转发去掉前缀
     */
    @TableField("strip_prefix")
    private String stripPrefix;
    /**
     * 是否重试
     */
    private String retryable;
    /**
     * 是否启用
     */
    private String enabled;
    /**
     * 敏感请求头
     */
    @TableField("sensitiveHeaders_list")
    private String sensitiveheadersList;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;
    /**
     * 删除标识（0-正常,1-删除）
     */
    @TableField("del_flag")
    private String delFlag;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}

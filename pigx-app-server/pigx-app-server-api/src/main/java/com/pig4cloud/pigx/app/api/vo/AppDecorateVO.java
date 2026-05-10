package com.pig4cloud.pigx.app.api.vo;

import com.pig4cloud.pigx.app.api.entity.AppPageEntity;
import com.pig4cloud.pigx.app.api.entity.AppTabbarEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * APP 装修页聚合数据。
 * <p>
 * 对外保持页面字段在顶层，额外追加 {@link #tabbar}，避免把底部导航配置重新写回
 * {@code app_page.page_data}。
 *
 * @author lengleng
 */
@Data
@Schema(description = "APP 装修页聚合数据")
public class AppDecorateVO {

    /**
     * 主键
     */
    @Schema(description = "主键")
    private Long id;

    /**
     * 页面类型
     */
    @Schema(description = "页面类型")
    private Integer pageType;

    /**
     * 页面名称
     */
    @Schema(description = "页面名称")
    private String pageName;

    /**
     * 页面装修组件 JSON，不包含底部 Tabbar。
     */
    @Schema(description = "页面数据")
    private String pageData;

    /**
     * 创建人
     */
    @Schema(description = "创建人")
    private String createBy;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 修改人
     */
    @Schema(description = "修改人")
    private String updateBy;

    /**
     * 修改时间
     */
    @Schema(description = "修改时间")
    private LocalDateTime updateTime;

    /**
     * 删除标记
     */
    @Schema(description = "删除标记")
    private String delFlag;

    /**
     * 当前查询场景下可用的底部导航栏。
     * <p>
     * 后台装修查询返回完整 Tabbar；App 运行时查询返回公开 Tabbar + 当前角色授权 Tabbar。
     */
    @Schema(description = "底部导航栏")
    private List<AppTabbarEntity> tabbar;

    /**
     * 根据页面实体和 Tabbar 列表构建装修聚合 VO。
     *
     * @param page   页面实体，可能为空
     * @param tabbar Tabbar 列表
     * @return 装修聚合数据
     */
    public static AppDecorateVO of(AppPageEntity page, List<AppTabbarEntity> tabbar) {
        AppDecorateVO vo = new AppDecorateVO();
        if (page != null) {
            BeanUtils.copyProperties(page, vo);
        }
        vo.setTabbar(tabbar);
        return vo;
    }

}

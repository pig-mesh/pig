package com.pig4cloud.pigx.app.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pig4cloud.pigx.app.api.entity.AppArticleEntity;
import com.pig4cloud.pigx.app.api.entity.AppPageEntity;
import com.pig4cloud.pigx.app.api.entity.AppTabbarEntity;
import com.pig4cloud.pigx.app.api.enums.PageTypeEnums;
import com.pig4cloud.pigx.app.api.vo.AppDecorateVO;
import com.pig4cloud.pigx.app.mapper.AppArticleMapper;
import com.pig4cloud.pigx.app.mapper.AppPageMapper;
import com.pig4cloud.pigx.app.mapper.AppTabbarMapper;
import com.pig4cloud.pigx.app.service.AppIndexService;
import com.pig4cloud.pigx.app.service.AppRoleTabbarService;
import com.pig4cloud.pigx.common.core.constant.CommonConstants;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * App首页服务实现类
 * 实现AppIndexService接口，提供首页数据、导航栏配置以及页面配置等功能。
 * <p>
 * 该实现面向移动端运行时：Tabbar 会按公开/登录角色授权规则过滤，
 * 页面装修数据则保持原始组件 JSON，不在这里做页面组件级权限裁剪。
 *
 * @author lengleng
 * @date 2023/6/8
 */
@Service
@RequiredArgsConstructor
public class AppIndexServiceImpl implements AppIndexService {

    private final AppTabbarMapper appTabbarMapper;

    private final AppPageMapper appPageMapper;

    private final AppArticleMapper appArticleMapper;

    private final AppRoleTabbarService appRoleTabbarService;

    /**
     * 获取商城首页基础数据。
     * <p>
     * 旧版首页接口返回首页装修页记录和资讯列表；新版装修页推荐使用 {@link #decorate(Integer)}。
     *
     * @return 首页页面数据和资讯列表
     */
    @Override
    public Map<String, Object> index() {
        Map<String, Object> response = new LinkedHashMap<>();
        AppPageEntity appPageEntity = appPageMapper.selectOne(Wrappers.<AppPageEntity>lambdaQuery()
                        .eq(AppPageEntity::getPageType, PageTypeEnums.HOME.getPageType())
                        .orderByDesc(AppPageEntity::getCreateTime),
                false
        );

        List<AppArticleEntity> articleList = appArticleMapper
                .selectList(Wrappers.<AppArticleEntity>lambdaQuery().orderByDesc(AppArticleEntity::getSort));
        response.put("pages", appPageEntity);
        response.put("article", articleList);
        return response;
    }

    /**
     * 获取运行时配置。
     * <p>
     * 当前只包含 Tabbar，并按 {@link #listAuthorizedTabbar()} 的规则过滤。
     *
     * @return 运行时配置集合
     */
    @Override
    public Map<String, Object> config() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("tabbar", listAuthorizedTabbar());
        return response;
    }

    /**
     * 根据页面类型获取最新装修页，并附加当前访问身份可见的 Tabbar。
     *
     * @param pageType 页面类型
     * @return 装修页聚合数据
     */
    @Override
    public AppDecorateVO decorate(Integer pageType) {
        AppPageEntity appPageEntity = appPageMapper.selectOne(
                Wrappers.<AppPageEntity>lambdaQuery()
                        .eq(AppPageEntity::getPageType, pageType)
                        .orderByDesc(AppPageEntity::getCreateTime),
                false
        );
        return AppDecorateVO.of(appPageEntity, listAuthorizedTabbar());
    }


    /**
     * 获取工作台页面数据。
     * <p>
     * 工作台内部按钮权限复用系统菜单 permission，这里不再读取 App 专属菜单授权。
     *
     * @return 工作台页面实体
     */
    @Override
    public AppPageEntity workbench() {
        return appPageMapper.selectOne(
                Wrappers.<AppPageEntity>lambdaQuery()
                        .eq(AppPageEntity::getPageType, PageTypeEnums.WORKBENCH.getPageType())
                        .orderByDesc(AppPageEntity::getCreateTime),
                false
        );
    }

    /**
     * 查询当前访问身份可见的 Tabbar。
     * <p>
     * 匿名访问只返回 {@code loginFlag=0} 的公开 Tabbar；登录访问从 token authorities
     * 解析角色 ID，再读取 {@code app_role_tabbar} 中授权的 Tabbar，避免跨库 join upms 的
     * {@code sys_user_role}。
     *
     * @return 当前可见的 Tabbar 列表
     */
    private List<AppTabbarEntity> listAuthorizedTabbar() {
        List<Long> roleIds = SecurityUtils.getUser() == null ? List.of() : SecurityUtils.getRoleIds();
        List<Long> tabbarIds = appRoleTabbarService.listTabbarIdsByRoleIds(roleIds);
        return appTabbarMapper.selectList(Wrappers.<AppTabbarEntity>lambdaQuery()
                .and(wrapper -> {
                    wrapper.eq(AppTabbarEntity::getLoginFlag, CommonConstants.STATUS_NORMAL);
                    if (!tabbarIds.isEmpty()) {
                        wrapper.or().in(AppTabbarEntity::getId, tabbarIds);
                    }
                })
                .orderByAsc(AppTabbarEntity::getSortOrder)
				.orderByAsc(AppTabbarEntity::getId));
	}
}

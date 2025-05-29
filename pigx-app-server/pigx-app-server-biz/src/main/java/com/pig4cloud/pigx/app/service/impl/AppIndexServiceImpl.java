package com.pig4cloud.pigx.app.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pig4cloud.pigx.app.api.entity.AppPageEntity;
import com.pig4cloud.pigx.app.api.entity.AppRole;
import com.pig4cloud.pigx.app.api.entity.AppTabbarEntity;
import com.pig4cloud.pigx.app.api.enums.PageTypeEnums;
import com.pig4cloud.pigx.app.mapper.AppPageMapper;
import com.pig4cloud.pigx.app.mapper.AppRoleMapper;
import com.pig4cloud.pigx.app.mapper.AppTabbarMapper;
import com.pig4cloud.pigx.app.service.AppIndexService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * app 页面控制
 *
 * @author lengleng
 * @date 2023/6/8
 */

@Service
@RequiredArgsConstructor
public class AppIndexServiceImpl implements AppIndexService {

    private final AppTabbarMapper appTabbarMapper;

    private final AppRoleMapper appRoleMapper;

    private final AppPageMapper appPageMapper;

    /**
     * 商城首页 首页数据
     */
    @Override
    public Map<String, Object> index() {
        Map<String, Object> response = new LinkedHashMap<>();
        AppPageEntity appPageEntity = appPageMapper.selectOne(Wrappers.<AppPageEntity>lambdaQuery()
                        .eq(AppPageEntity::getPageType, PageTypeEnums.HOME.getPageType())
                        .orderByDesc(AppPageEntity::getCreateTime),
                false
        );
        response.put("pages", appPageEntity);
        return response;
    }

    /**
     * 导航栏配置
     *
     * @return Map<String, Object>
     */
    @Override
    public Map<String, Object> config() {
        Map<String, Object> response = new LinkedHashMap<>();
        List<AppTabbarEntity> tabbarEntityList = appTabbarMapper
                .selectList(Wrappers.<AppTabbarEntity>lambdaQuery().orderByAsc(AppTabbarEntity::getSortOrder));
        response.put("tabbar", tabbarEntityList);
        return response;
    }

    /**
     * 根据类型查询页面配置
     *
     * @param pageType 页面类型
     * @return {@link AppPageEntity }
     */
    @Override
    public AppPageEntity decorate(Integer pageType) {
        AppPageEntity appPage = appPageMapper.selectOne(
                Wrappers.<AppPageEntity>lambdaQuery()
                        .eq(AppPageEntity::getPageType, pageType)
                        .orderByDesc(AppPageEntity::getCreateTime),
                false
        );

        // 如果是工作台页面类型
        if (!Objects.equals(PageTypeEnums.WORKBENCH.getPageType(), pageType) || appPage == null) {
            return appPage;
        }

        AppRole appRole = appRoleMapper.selectById(1); // 角色ID根据实际业务调整
        List<String> menuIdList = StrUtil.split(appRole.getMenuId(), CharUtil.COMMA);

        if (CollUtil.isEmpty(menuIdList)) {
            return appPage;
        }

        JSONArray pageDataArray = JSONUtil.parseArray(appPage.getPageData());
        for (Object groupObj : pageDataArray) {
            JSONObject group = (JSONObject) groupObj;
            JSONArray itemArray = group.getJSONObject("content").getJSONArray("data");

            // 使用 removeIf 进行过滤
            itemArray.removeIf(item -> {
                String menuId = ((JSONObject) item).getStr(AppPageEntity.Fields.id);
                return StrUtil.isNotBlank(menuId) && !menuIdList.contains(menuId);
            });
        }

        appPage.setPageData(pageDataArray.toStringPretty());
        return appPage;
    }

}

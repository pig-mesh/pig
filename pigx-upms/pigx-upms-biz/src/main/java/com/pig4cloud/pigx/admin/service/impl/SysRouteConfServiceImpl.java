/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.api.entity.SysRouteConf;
import com.pig4cloud.pigx.admin.mapper.SysRouteConfMapper;
import com.pig4cloud.pigx.admin.service.SysRouteConfService;
import com.pig4cloud.pigx.common.gateway.support.DynamicRouteInitEvent;
import com.pig4cloud.pigx.common.gateway.vo.RouteDefinitionVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * @author lengleng
 * @date 2018年11月06日10:27:55
 * <p>
 * 动态路由处理类
 */
@Slf4j
@AllArgsConstructor
@Service("sysRouteConfService")
public class SysRouteConfServiceImpl extends ServiceImpl<SysRouteConfMapper, SysRouteConf>
        implements SysRouteConfService {

    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * 更新路由信息
     *
     * @param route 路由信息
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Void> addOrUpdateRoute(JSONObject route) {
        try {
            log.info("更新路由 ->{}", route);
            RouteDefinitionVo vo = new RouteDefinitionVo();

            String id = route.getStr("routeId");
            if (StrUtil.isNotBlank(id)) {
                vo.setId(id);
            }

            String routeName = route.getStr("routeName");
            if (StrUtil.isNotBlank(routeName)) {
                vo.setRouteName(routeName);
            }

            JSONArray predicates = route.getJSONArray("predicates");
            if (predicates != null) {
                List<PredicateDefinition> predicateDefinitionList = predicates.toList(PredicateDefinition.class);
                vo.setPredicates(predicateDefinitionList);
            }

            JSONArray filters = route.getJSONArray("filters");
            if (filters != null) {
                List<FilterDefinition> filterDefinitionList = filters.toList(FilterDefinition.class);
                vo.setFilters(filterDefinitionList);
            }

            Object uri = route.getStr("uri");
            if (uri != null) {
                vo.setUri(URI.create(String.valueOf(uri)));
            }

            Integer order = route.getInt("order");
            if (order != null) {
                vo.setOrder(order);
            }

            Map<String, Object> metadataMap = route.getBean("metadata", Map.class);
            if (metadataMap != null) {
                vo.setMetadata(metadataMap);
            }

            // 逻辑删除 ，不直接更新避免危险！
            this.remove(Wrappers.<SysRouteConf>lambdaQuery().eq(SysRouteConf::getRouteId, id));

            // 插入生效路由
            SysRouteConf routeConf = new SysRouteConf();
            routeConf.setRouteId(vo.getId());
            routeConf.setRouteName(vo.getRouteName());
            routeConf.setFilters(JSONUtil.toJsonStr(vo.getFilters()));
            routeConf.setPredicates(JSONUtil.toJsonStr(vo.getPredicates()));
            routeConf.setSortOrder(vo.getOrder());
            routeConf.setUri(vo.getUri().toString());
            routeConf.setMetadata(JSONUtil.toJsonStr(vo.getMetadata()));
            this.save(routeConf);
            log.debug("更新网关路由结束 ");
        } catch (Exception e) {
            log.error("路由配置解析失败", e);
            // 抛出异常
            throw new RuntimeException(e);
        } finally {
            this.applicationEventPublisher.publishEvent(new DynamicRouteInitEvent(this));
        }
        return Mono.empty();
    }

    /**
     * 保存路由
     *
     * @param routeConf 路由配置
     * @return {@link SysRouteConf }
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysRouteConf saveRoute(SysRouteConf routeConf) {
        baseMapper.insert(routeConf);
        this.applicationEventPublisher.publishEvent(new DynamicRouteInitEvent(this));
        return routeConf;
    }

}

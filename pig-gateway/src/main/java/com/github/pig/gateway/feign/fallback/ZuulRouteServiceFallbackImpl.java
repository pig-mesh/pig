package com.github.pig.gateway.feign.fallback;

import com.github.pig.common.entity.SysZuulRoute;
import com.github.pig.common.vo.MenuVO;
import com.github.pig.gateway.feign.MenuService;
import com.github.pig.gateway.feign.ZuulRouteService;
import com.xiaoleilu.hutool.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author lengleng
 * @date 2018/5/15
 * 远程调用路由接口异常回调
 */
@Slf4j
@Service
public class ZuulRouteServiceFallbackImpl implements ZuulRouteService {

    /**
     * 调用upms查询全部的路由配置
     *
     * @return 路由配置表
     */
    @Override
    public List<SysZuulRoute> findAllZuulRoute() {
        log.error("获取远程路由配置失败");
        return CollUtil.newArrayList();
    }
}

package com.pig4cloud.pigx.app.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.app.api.entity.AppArticleCollectEntity;
import com.pig4cloud.pigx.app.api.entity.AppArticleEntity;
import com.pig4cloud.pigx.app.mapper.AppArticleCollectMapper;
import com.pig4cloud.pigx.app.mapper.AppArticleMapper;
import com.pig4cloud.pigx.app.service.AppArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 文章资讯
 *
 * @author pig
 * @date 2023-06-07 16:32:35
 */
@Service
@RequiredArgsConstructor
public class AppArticleServiceImpl extends ServiceImpl<AppArticleMapper, AppArticleEntity> implements AppArticleService {

    private final AppArticleCollectMapper collectMapper;


    /**
     * 获取文章并使阅读数+1
     *
     * @param id id
     * @return
     */
    @Override
    public AppArticleEntity getArticleAndIncrById(Long id, Long userId) {
        AppArticleEntity appArticleEntity = baseMapper.selectById(id);
        // 查询是否收藏了
        if (Objects.nonNull(userId)) {
            boolean exists = collectMapper.exists(Wrappers.<AppArticleCollectEntity>lambdaQuery()//
                    .eq(AppArticleCollectEntity::getArticleId, appArticleEntity.getId())//
                    .eq(AppArticleCollectEntity::getUserId, userId));
            appArticleEntity.setCollect(exists);
        }

        //TODO 更新条件需要根据其他指数限制
        Integer nowVisit = appArticleEntity.getVisit();
        appArticleEntity.setVisit(nowVisit + 1);
        // 乐观锁
        baseMapper.update(appArticleEntity, Wrappers.<AppArticleEntity>lambdaQuery().eq(AppArticleEntity::getId, id).eq(AppArticleEntity::getVisit, nowVisit));
        return appArticleEntity;
    }
}

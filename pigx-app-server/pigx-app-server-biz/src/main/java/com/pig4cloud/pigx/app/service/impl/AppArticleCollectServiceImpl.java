package com.pig4cloud.pigx.app.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.app.api.entity.AppArticleCollectEntity;
import com.pig4cloud.pigx.app.mapper.AppArticleCollectMapper;
import com.pig4cloud.pigx.app.service.AppArticleCollectService;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import org.springframework.stereotype.Service;

/**
 * 文章收藏服务实现。
 * <p>
 * 收藏归属当前登录用户，保存前会校验同一用户是否已经收藏过同一文章。
 *
 * @author pig
 * @date 2023-06-16 14:33:41
 */
@Service
public class AppArticleCollectServiceImpl extends ServiceImpl<AppArticleCollectMapper, AppArticleCollectEntity>
        implements AppArticleCollectService {

    /**
     * 保存当前登录用户的文章收藏。
     *
     * @param appArticleCollect 收藏请求，业务上只需要文章 ID，用户 ID 由登录态写入
     * @return true=收藏成功，false=已经收藏
     */
    @Override
    public Boolean saveArticleCollect(AppArticleCollectEntity appArticleCollect) {
        Long id = SecurityUtils.getUser().getId();
        appArticleCollect.setUserId(id);

        if (baseMapper.exists(Wrappers.<AppArticleCollectEntity>lambdaQuery()
                .eq(AppArticleCollectEntity::getUserId, id)
                .eq(AppArticleCollectEntity::getArticleId, appArticleCollect.getArticleId()))) {
            return Boolean.FALSE;
        }

        return this.save(appArticleCollect);

    }

}

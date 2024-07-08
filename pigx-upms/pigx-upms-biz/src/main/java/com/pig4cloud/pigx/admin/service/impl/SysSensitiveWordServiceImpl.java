package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import com.pig4cloud.pigx.admin.api.entity.SysSensitiveWordEntity;
import com.pig4cloud.pigx.admin.mapper.SysSensitiveWordMapper;
import com.pig4cloud.pigx.admin.service.SysSensitiveWordService;
import com.pig4cloud.pigx.common.core.constant.CacheConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 敏感词
 *
 * @author pig
 * @date 2024-07-06 15:54:43
 */
@Service
@RequiredArgsConstructor
public class SysSensitiveWordServiceImpl extends ServiceImpl<SysSensitiveWordMapper, SysSensitiveWordEntity> implements SysSensitiveWordService {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 查询敏感词
     *
     * @param sysSensitiveWord 敏感词
     * @return 敏感词列表
     */
    @Override
    public List<String> matchSensitiveWord(SysSensitiveWordEntity sysSensitiveWord) {
        SensitiveWordBs sensitiveWordBs = SpringUtil.getBean(SensitiveWordBs.class);
        return sensitiveWordBs.findAll(sysSensitiveWord.getSensitiveWord());
    }

    /**
     * 保存敏感词
     *
     * @param sysSensitiveWord 敏感词
     * @return success/false
     */
    @Override
    public Boolean saveSensitive(SysSensitiveWordEntity sysSensitiveWord) {
        List<SysSensitiveWordEntity> wordEntityList = baseMapper.selectList(Wrappers.<SysSensitiveWordEntity>lambdaQuery().
                eq(SysSensitiveWordEntity::getSensitiveWord, sysSensitiveWord.getSensitiveWord()));

        if (CollUtil.isEmpty(wordEntityList)) {
            save(sysSensitiveWord);
        } else {
            wordEntityList.forEach(wordEntity -> {
                wordEntity.setSensitiveType(sysSensitiveWord.getSensitiveType());
                updateById(wordEntity);
            });
        }

        redisTemplate.convertAndSend(CacheConstants.SENSITIVE_REDIS_RELOAD_TOPIC, "刷新敏感词缓存");
        return Boolean.TRUE;
    }
}

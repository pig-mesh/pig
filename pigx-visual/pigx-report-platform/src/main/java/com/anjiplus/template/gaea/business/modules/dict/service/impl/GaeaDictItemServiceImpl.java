package com.anjiplus.template.gaea.business.modules.dict.service.impl;

import com.anji.plus.gaea.cache.CacheHelper;
import com.anji.plus.gaea.constant.BaseOperationEnum;
import com.anji.plus.gaea.constant.GaeaConstant;
import com.anji.plus.gaea.constant.GaeaKeyConstant;
import com.anji.plus.gaea.curd.mapper.GaeaBaseMapper;
import com.anji.plus.gaea.exception.BusinessException;
import com.anjiplus.template.gaea.business.modules.dict.dao.GaeaDictItemMapper;
import com.anjiplus.template.gaea.business.modules.dict.dao.entity.GaeaDictItem;
import com.anjiplus.template.gaea.business.modules.dict.service.GaeaDictItemService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 数据字典项(GaeaDictItem)ServiceImpl
 *
 * @author lirui
 * @since 2021-03-10 13:05:59
 */
@Service
public class GaeaDictItemServiceImpl implements GaeaDictItemService {
    @Autowired
    private GaeaDictItemMapper gaeaDictItemMapper;

    @Autowired
    private CacheHelper cacheHelper;

    @Override
    public GaeaBaseMapper<GaeaDictItem> getMapper() {
        return  gaeaDictItemMapper;
    }


    @Override
    public void processAfterOperation(GaeaDictItem entity, BaseOperationEnum operationEnum) throws BusinessException {
        String dictCode = entity.getDictCode();
        String locale = entity.getLocale();

        String key = GaeaKeyConstant.DICT_PREFIX + locale + GaeaConstant.REDIS_SPLIT + dictCode;
        switch (operationEnum) {
            case INSERT:
            case UPDATE:
                cacheHelper.hashSet(key, entity.getItemValue(), entity.getItemName());
                break;
            case DELETE:
                cacheHelper.hashDel(key, entity.getItemValue());
                default:
        }
    }

    @Override
    public void processBatchAfterOperation(List<GaeaDictItem> entities, BaseOperationEnum operationEnum) throws BusinessException {
        if (CollectionUtils.isEmpty(entities)) {
            return;
        }

        Map<String, Map<String, String>> dictItemMap = entities.stream()
                .collect(Collectors.groupingBy(item -> item.getLocale() + GaeaConstant.REDIS_SPLIT +item.getDictCode(),
                                Collectors.toMap(GaeaDictItem::getItemValue, GaeaDictItem::getItemName,(v1,v2)-> v2)));

        switch (operationEnum) {
            case DELETE_BATCH:
                //遍历并保持到Redis中
                dictItemMap.entrySet().stream().forEach(entry -> {
                    String key = GaeaKeyConstant.DICT_PREFIX  + entry.getKey();
                    Set<String> hashKeys = entry.getValue().keySet();
                    cacheHelper.hashBatchDel(key, hashKeys);
                });
                break;
                default:
        }
    }

    @Override
    public Map<String, String> getItemMap(String dictCode) {
        Locale locale = LocaleContextHolder.getLocale();

        LambdaQueryWrapper<GaeaDictItem> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(GaeaDictItem::getDictCode, dictCode);
        wrapper.eq(GaeaDictItem::getLocale, locale.getLanguage());

        List<GaeaDictItem> list = list(wrapper);
        Map<String, String> data = list.stream().collect(Collectors.toMap(GaeaDictItem::getItemValue, GaeaDictItem::getItemName, (v1, v2) -> v2));
        return data;
    }

}

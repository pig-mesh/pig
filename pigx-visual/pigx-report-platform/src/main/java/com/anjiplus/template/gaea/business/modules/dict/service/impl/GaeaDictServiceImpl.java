package com.anjiplus.template.gaea.business.modules.dict.service.impl;

import com.anji.plus.gaea.bean.KeyValue;
import com.anji.plus.gaea.cache.CacheHelper;
import com.anji.plus.gaea.constant.Enabled;
import com.anji.plus.gaea.constant.GaeaConstant;
import com.anji.plus.gaea.constant.GaeaKeyConstant;
import com.anji.plus.gaea.curd.mapper.GaeaBaseMapper;
import com.anji.plus.gaea.utils.GaeaUtils;
import com.anjiplus.template.gaea.business.modules.dict.dao.GaeaDictItemMapper;
import com.anjiplus.template.gaea.business.modules.dict.dao.GaeaDictMapper;
import com.anjiplus.template.gaea.business.modules.dict.dao.entity.GaeaDict;
import com.anjiplus.template.gaea.business.modules.dict.dao.entity.GaeaDictItem;
import com.anjiplus.template.gaea.business.modules.dict.service.GaeaDictService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * (GaeaDict)ServiceImpl
 *
 * @author lr
 * @since 2021-02-23 10:01:02
 */
@Service
public class GaeaDictServiceImpl implements GaeaDictService {

    @Autowired
    private GaeaDictMapper gaeaDictMapper;

    @Autowired
    private GaeaDictItemMapper gaeaDictItemMapper;

    @Autowired
    private CacheHelper cacheHelper;

    @Override
    public GaeaBaseMapper<GaeaDict> getMapper() {
        return  gaeaDictMapper;
    }

    /**
     * 刷新全部缓存
     * @param dictCodes
     */
    @Override
    public void refreshCache(List<String> dictCodes) {

        //查询指定字典项
        List<GaeaDictItem> gaeaDictItems = findItems(dictCodes);

        //对数据字典项进行分组，分组key=语言标识:字典编码
        Map<String, Map<String, String>> dictItemMap = gaeaDictItems.stream()
                .collect(Collectors
                        .groupingBy(item -> item.getLocale() + GaeaConstant.REDIS_SPLIT +item.getDictCode(),
                                Collectors.toMap(GaeaDictItem::getItemValue, GaeaDictItem::getItemName,(v1,v2)-> v2)));

        //遍历并保持到Redis中
        dictItemMap.entrySet().stream().forEach(entry -> {
            String key = GaeaKeyConstant.DICT_PREFIX  + entry.getKey();
            cacheHelper.delete(key);
            cacheHelper.hashSet(key, entry.getValue());
        });
    }

    /**
     * 根据国际化语言查询指定字典编码下拉列表
     * 先从Redis中获取
     * @param dictCode 字典名称
     * @param language 语言
     * @return
     */
    @Override
    public List<KeyValue> select(String dictCode, String language) {

        //缓存字典Key
        String dictKey = GaeaKeyConstant.DICT_PREFIX  + language + GaeaConstant.REDIS_SPLIT + dictCode;

        Map<String, String> dictMap = cacheHelper.hashGet(dictKey);

        //当查询的字典为空时
        if (CollectionUtils.isEmpty(dictMap)) {
            LambdaQueryWrapper<GaeaDictItem> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(GaeaDictItem::getDictCode, dictCode)
                    .eq(GaeaDictItem::getEnabled, Enabled.YES.getValue())
                    .eq(GaeaDictItem::getLocale, language)
                    .orderByAsc(GaeaDictItem::getSort);

            List<GaeaDictItem> list = gaeaDictItemMapper.selectList(wrapper);

            List<KeyValue> keyValues = list.stream()
                    .map(dictItemEntity -> new KeyValue(dictItemEntity.getItemValue(), dictItemEntity.getItemName(), dictItemEntity.getItemExtend()))
                    .collect(Collectors.toList());
            //当缓存不存在时，刷新缓存
            List<String> dictCodes = new ArrayList<>();
            dictCodes.add(dictCode);
            refreshCache(dictCodes);
            return keyValues;
        }

        List<KeyValue> keyValues = GaeaUtils.formatKeyValue(dictMap);

        //添加扩展字段
        LambdaQueryWrapper<GaeaDictItem> gaeaDictItemWrapper = Wrappers.lambdaQuery();
        gaeaDictItemWrapper.eq(GaeaDictItem::getDictCode, dictCode);
        gaeaDictItemWrapper.isNotNull(GaeaDictItem::getItemExtend);

        Map<String, String> extendMap = gaeaDictItemMapper.selectList(gaeaDictItemWrapper).stream()
                .filter(gaeaDictItem -> StringUtils.isNotBlank(gaeaDictItem.getItemExtend()))
                .collect(Collectors.toMap(GaeaDictItem::getItemValue, GaeaDictItem::getItemExtend, (v1, v2) -> v2));
        if (!CollectionUtils.isEmpty(extendMap)) {
            keyValues.stream().forEach(keyValue -> keyValue.setExtend(extendMap.get(keyValue.getId())));
        }
        return keyValues;
    }

    @Override
    public List<GaeaDictItem> findItems(List<String> dictCodes) {

        LambdaQueryWrapper<GaeaDictItem> gaeaDictItemQueryWrapper = Wrappers.lambdaQuery();
        gaeaDictItemQueryWrapper.eq(GaeaDictItem::getEnabled, Enabled.YES.getValue());
        if (!CollectionUtils.isEmpty(dictCodes)) {
            gaeaDictItemQueryWrapper.in(GaeaDictItem::getDictCode, dictCodes);
        }
        return gaeaDictItemMapper.selectList(gaeaDictItemQueryWrapper);
    }


    @Override
    public Collection<KeyValue> selectTypeCode(String system, String language) {

        //缓存字典Key
        String dictKey = GaeaKeyConstant.DICT_PREFIX + language + GaeaConstant.REDIS_SPLIT + system;

        Map<String, String> dictMap = cacheHelper.hashGet(dictKey);

        //当查询的字典为空时
        if (CollectionUtils.isEmpty(dictMap)) {
            LambdaQueryWrapper<GaeaDict> wrapper = Wrappers.lambdaQuery();
            //wrapper.eq(GaeaDict::getLocale, language).orderByAsc(GaeaDict::getSort);

            List<GaeaDict> list = getMapper().selectList(wrapper);

            Set<KeyValue> keyValues = list.stream()
                    .map(dictItemEntity -> new KeyValue(dictItemEntity.getDictCode(),
                            dictItemEntity.getDictName()))
                    .collect(Collectors.toSet());
            return keyValues;
        }

        return GaeaUtils.formatKeyValue(dictMap);
    }


    @Override
    public Map<String, List<KeyValue>> all(String language) {
        LambdaQueryWrapper<GaeaDictItem> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(GaeaDictItem::getEnabled, Enabled.YES.getValue())
                .eq(GaeaDictItem::getLocale, language)
                .orderByAsc(GaeaDictItem::getSort);

        List<GaeaDictItem> list = gaeaDictItemMapper.selectList(wrapper);
        Map<String, List<KeyValue>> all = list.stream().collect(
                Collectors.groupingBy(
                        GaeaDictItem::getDictCode,
                        Collectors.mapping(dictItemEntity -> {
                            Object itemValue = null;
                            try{
                                itemValue = Integer.parseInt(dictItemEntity.getItemValue());
                            }catch (Exception e){
                                itemValue = dictItemEntity.getItemValue();
                            }
                            return new KeyValue(itemValue, dictItemEntity.getItemName(), dictItemEntity.getItemExtend());
                        },Collectors.toList())));
        return all;
    }

}

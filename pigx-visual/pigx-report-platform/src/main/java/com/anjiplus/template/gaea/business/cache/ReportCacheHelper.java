package com.anjiplus.template.gaea.business.cache;


import com.anji.plus.gaea.cache.CacheHelper;
import com.google.common.collect.Maps;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ReportCacheHelper implements CacheHelper, ApplicationContextAware {

    @Autowired
    private Cache cache;

    @Override
    public String stringGet(String key) {
        Cache.ValueWrapper valueWrapper = cache.get(key);
        if (valueWrapper != null) {
            return (String) valueWrapper.get();
        }
        return CacheHelper.super.stringGet(key);
    }

    @Override
    public Boolean setIfAbsent(String key, String value) {
        cache.putIfAbsent(key, value);
        return true;
    }


    @Override
    public boolean exist(String key) {
        String cacheHoldTime = stringGet(key + "_HoldTime");
        if (cacheHoldTime != null && Long.parseLong(cacheHoldTime) > 0) {
            if (Long.parseLong(cacheHoldTime) < System.currentTimeMillis()) {
                delete(key + "_HoldTime");
                delete(key);
                return false;
            }
        }
        return cache.get(key) != null;
    }


    @Override
    public void stringSet(String key, String value) {
        cache.put(key, value);
    }


    @Override
    public String regKey(String key) {
        return CacheHelper.super.regKey(key);
    }

    @Override
    public void stringSetExpire(String key, String value, long seconds) {
        stringSet(key, value);
        if (seconds > 0) {
            //缓存失效时间
            stringSet(key + "_HoldTime", String.valueOf(System.currentTimeMillis() + seconds * 1000));
        }
    }

    @Override
    public Map<String, String> hashGet(String key) {
        Cache.ValueWrapper t = cache.get(key);
        if (t != null) {
            return (Map<String, String>) t.get();
        }
        return Maps.newHashMap();
    }

    @Override
    public String hashGetString(String key, String hashKey) {
        Map<String, String> stringStringMap = hashGet(key);
        return stringStringMap.get(hashKey);
    }

    @Override
    public void hashDel(String key, String hashKey) {
        Map<String, String> stringStringMap = hashGet(key);
        stringStringMap.remove(hashKey);
    }

    @Override
    public void hashBatchDel(String key, Set<String> hashKeys) {
        Map<String, String> stringStringMap = hashGet(key);
        hashKeys.forEach(stringStringMap::remove);
    }

    @Override
    public boolean hashExist(String key, String hashKey) {
        if (exist(key)) {
            Map<String, String> map = hashGet(key);
            return map.containsKey(hashKey);
        }
        return false;
    }

    @Override
    public boolean hashAnyExist(String key, String[] hashKeys) {
        return CacheHelper.super.hashAnyExist(key, hashKeys);
    }

    @Override
    public void hashSet(String key, String hashKey, String hashValue) {
        Map<String, String> map;
        if (exist(key)) {
            map = hashGet(key);
        } else {
            map = new HashMap<>();
        }
        map.put(hashKey, hashValue);
        hashSet(key, map);
    }

    @Override
    public void hashSet(String key, Map<String, String> hash) {
        cache.put(key, hash);
    }

    @Override
    public boolean delete(String key) {
        if (exist(key)) {
            cache.evict(key);
        }
        return true;
    }

    @Override
    public boolean delete(List<String> keys) {
        keys.forEach(this::delete);
        return true;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        /*基于内存的本地缓存*/
        cache = (Cache) applicationContext.getBean("ehCacheCache");
    }
}

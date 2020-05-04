package com.mm.call.limit.service.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mm.call.limit.enums.LimitTypeEnum;
import com.mm.call.limit.service.CallLimitService;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: xiemingmin
 * @Description: 限流处理service
 * @Date: 2020/5/4 13:09
 */
public class CallLimitServiceImpl implements CallLimitService {

    private String redisUrl;

    private ConcurrentHashMap<String, Cache<String, Integer>> cacheMap = new ConcurrentHashMap();

    @Override
    public boolean allowCall(String userKey, LimitTypeEnum type, long time, TimeUnit timeUnit) {
        if (type == LimitTypeEnum.One_machine){
            Cache<String, Integer> cache = getCache(time, timeUnit);
            int callTime = cache.getIfPresent(userKey);
            if (callTime > 0){
                return false;
            } else {
                cache.put(userKey, 1);
            }
        }
        return true;
    }


    /**
     * 一时间为key创建不同的guavaCache
     *
     * @param time
     * @param timeUnit
     * @return
     */
    private Cache<String, Integer> getCache(long time, TimeUnit timeUnit) {
        String key = String.valueOf(time) + timeUnit.toString();
        Cache<String, Integer> cache = cacheMap.get(key);
        if (cache != null) {
            return cache;
        } else {
            cache = CacheBuilder.newBuilder().expireAfterWrite(time, timeUnit).build();
            // 使用putIfAbsent保证并发安全
            Cache<String, Integer> cacheOld = cacheMap.putIfAbsent(key, cache);
            if (cacheOld != null){
                cache = cacheOld;
            }
            return cache;
        }
    }

}

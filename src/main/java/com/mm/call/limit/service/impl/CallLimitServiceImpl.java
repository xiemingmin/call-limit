package com.mm.call.limit.service.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mm.call.limit.enums.LimitTypeEnum;
import com.mm.call.limit.handler.CallLimitAnnotationHandler;
import com.mm.call.limit.service.CallLimitService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: xiemingmin
 * @Description: 限流处理service
 * @Date: 2020/5/4 13:09
 */
public class CallLimitServiceImpl implements CallLimitService, ApplicationContextAware, InitializingBean {

    private String redisUrl;

    private ConcurrentHashMap<String, Cache<String, Integer>> cacheMap = new ConcurrentHashMap();

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public boolean allowCall(String methodFlag, String userKey, LimitTypeEnum type, long time, TimeUnit timeUnit) {
        String cacheKey = methodFlag + userKey;
        if (type == LimitTypeEnum.One_machine) {
            Cache<String, Integer> cache = getCache(time, timeUnit);
            Integer callTime = cache.getIfPresent(cacheKey);
            if (callTime != null && callTime > 0) {
                return false;
            } else {
                cache.put(cacheKey, 1);
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
            if (cacheOld != null) {
                cache = cacheOld;
            }
            return cache;
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(CallLimitAnnotationHandler.class);
        GenericBeanDefinition beanDefinition = (GenericBeanDefinition) beanDefinitionBuilder.getBeanDefinition();
        beanDefinition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_NAME);
        ((DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory()).registerBeanDefinition("callLimitAnnotationHandler", beanDefinition);
    }

    public String getRedisUrl() {
        return redisUrl;
    }

    public void setRedisUrl(String redisUrl) {
        this.redisUrl = redisUrl;
    }
}

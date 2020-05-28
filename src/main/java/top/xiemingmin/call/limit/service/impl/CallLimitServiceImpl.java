package top.xiemingmin.call.limit.service.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import top.xiemingmin.call.limit.client.RedisClient;
import top.xiemingmin.call.limit.enums.LimitTypeEnum;
import top.xiemingmin.call.limit.handler.CallLimitAnnotationHandler;
import top.xiemingmin.call.limit.service.CallLimitService;
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

    private RedisClient redisClient;

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
        } else if (type == LimitTypeEnum.Distributed) {
            if (redisClient == null){
                throw new RuntimeException("---使用分布式限流请配置jedisPool！");
            }
            String value = redisClient.get(cacheKey);
            if (value != null && value != "" && Integer.valueOf(value) > 0) {
                return false;
            } else {
                return redisClient.setNx(cacheKey, String.valueOf(1), time, timeUnit);
            }
        }
        return true;
    }


    /**
     * 以时间为key创建不同的guavaCache
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

    public void setRedisClient(RedisClient redisClient) {
        this.redisClient = redisClient;
    }
}

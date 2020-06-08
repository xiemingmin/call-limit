package top.xiemingmin.call.limit.client.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;
import top.xiemingmin.call.limit.client.RedisClient;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * *******************************************************************************************
 * ***            X I E M I N G M I N . T O P  ---  U N C L E  G E O R G E                 ***
 * *******************************************************************************************
 *
 * @author xiemingmin
 * Date: 2020/4/29 22:09
 * Description: 接口防刷工具
 */
public class RedisClientImpl implements RedisClient {

    private static final Logger logger = LoggerFactory.getLogger(RedisClientImpl.class);

    private JedisPool jedisPool;

    @PostConstruct
    public void initCache(){
        if (logger.isDebugEnabled()){
            logger.debug("RedisClientImpl#initCache 初始化redisClient成功！");
        }
    }

    /**
     * 获取Jedis实例
     *
     * @return
     */
    public synchronized Jedis getJedis() {
        if (jedisPool != null) {
            Jedis resource = jedisPool.getResource();
            if (resource == null) {
                throw new RuntimeException("RedisClientImpl#getJedis获取jedis实例为空！");
            }
            return resource;
        }
        throw new RuntimeException("RedisClientImpl#getJedis获取jedis实例失败！");
    }

    @Override
    public String get(String key) {
        Jedis jedis = getJedis();
        try {
            return jedis.get(key);
        } finally {
            jedis.close();
        }
    }

    @Override
    public boolean setNx(String key, String value, Long time, TimeUnit timeUnit) {
        Jedis jedis = getJedis();
        try {
            SetParams setParams = new SetParams();
            setParams.px(TimeUnit.MILLISECONDS.convert(time, timeUnit));
            setParams.nx();
            String result = getJedis().set(key, value, setParams);
            if ("OK".equalsIgnoreCase(result)) {
                return true;
            } else {
                return false;
            }
        } finally {
            jedis.close();
        }
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }
}

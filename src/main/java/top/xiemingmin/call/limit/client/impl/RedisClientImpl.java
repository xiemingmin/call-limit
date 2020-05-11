package top.xiemingmin.call.limit.client.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.params.SetParams;
import top.xiemingmin.call.limit.client.RedisClient;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: xiemingmin
 * @Description:
 * @Date: 2020/5/10 23:24
 */
public class RedisClientImpl implements RedisClient {

    private static final Logger logger = LoggerFactory.getLogger(RedisClientImpl.class);

    private String redisServeIp;

    private Integer redisServePort;

    private String auth;

    private JedisPool jedisPool;

    @PostConstruct
    public void initRedisPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        // 与redis连接池连接的最大连接数
        config.setMaxTotal(500);
        // 在jedis连接池中最大的idle状态（空闲的）的jedis实例的个数
        config.setMaxIdle(300);
        // 在jedis连接池中最小的idle状态（空闲的）的jedis实例的个数
        config.setMinIdle(50);
        // 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的
        config.setTestOnBorrow(true);
        jedisPool = new JedisPool(config, redisServeIp, redisServePort, 10000, auth);
        if (logger.isDebugEnabled()) {
            logger.debug("RedisClientImpl#initRedisPool redis连接池创建成功---> redisServeIp:{}, redisServePort:{}, auth:{}", redisServeIp, redisServePort, auth);
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
        throw new RuntimeException("RedisClientImpl#getJedis jedisPool实例为空！");
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

    public void setRedisServeIp(String redisServeIp) {
        this.redisServeIp = redisServeIp;
    }

    public void setRedisServePort(Integer redisServePort) {
        this.redisServePort = redisServePort;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }
}

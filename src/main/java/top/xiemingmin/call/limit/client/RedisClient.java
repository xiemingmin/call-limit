package top.xiemingmin.call.limit.client;

import java.util.concurrent.TimeUnit;

/**
 * @Auther: xiemingmin
 * @Description:
 * @Date: 2020/5/10 23:08
 */
public interface RedisClient {

    /**
     * redis String get
     *
     * @param key key
     * @return
     */
    String get(String key);


    boolean setNx(String key, String value, Long time, TimeUnit timeUnit);

}

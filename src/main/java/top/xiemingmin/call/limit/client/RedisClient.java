package top.xiemingmin.call.limit.client;

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

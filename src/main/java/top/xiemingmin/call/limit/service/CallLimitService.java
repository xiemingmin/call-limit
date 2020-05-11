package top.xiemingmin.call.limit.service;

import top.xiemingmin.call.limit.enums.LimitTypeEnum;

import java.util.concurrent.TimeUnit;

/**
 * @Auther: xiemingmin
 * @Description: 限流处理service
 * @Date: 2020/5/4 13:01
 */
public interface CallLimitService {

    /**
     * 是否允许调用
     *
     * @param methodFlag 调用方法标识
     * @param userKey    用户表示
     * @param type       限制类型
     * @param time       限制时间
     * @param timeUnit   限制时间单位
     * @return
     */
    boolean allowCall(String methodFlag, String userKey, LimitTypeEnum type, long time, TimeUnit timeUnit);

}
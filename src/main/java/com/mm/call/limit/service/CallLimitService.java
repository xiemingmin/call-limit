package com.mm.call.limit.service;

import com.mm.call.limit.enums.LimitTypeEnum;

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
     * @param userKey  用户表示
     * @param type     限制类型
     * @param time     限制时间
     * @param timeUnit 限制时间单位
     * @return
     */
    boolean allowCall(String userKey, LimitTypeEnum type, long time, TimeUnit timeUnit);

}

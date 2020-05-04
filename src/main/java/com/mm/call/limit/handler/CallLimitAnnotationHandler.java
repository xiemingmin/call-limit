package com.mm.call.limit.handler;

import com.mm.call.limit.annotation.CallLimit;
import com.mm.call.limit.enums.LimitTypeEnum;
import com.mm.call.limit.intf.UserInfoSupport;
import com.mm.call.limit.service.CallLimitService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: xiemingmin
 * @Description: callLimit 注解处理器
 * @Date: 2020/5/4 23:45
 */
@Aspect
public class CallLimitAnnotationHandler {

    private static final Logger logger = LoggerFactory.getLogger(CallLimitAnnotationHandler.class);

    @Autowired
    private CallLimitService callLimitService;
    @Autowired
    private UserInfoSupport userInfoSupport;


    @Before("@annotation(callLimit)")
    public void doBefore(JoinPoint joinPoint, CallLimit callLimit) {
        // 获取方法签名
        String methodName = joinPoint.getSignature().getName().toString();
        // 获取限制类型
        LimitTypeEnum type = callLimit.type();
        // 获取限制单位时间
        long time = callLimit.time();
        // 获取时间单位
        TimeUnit timeUnit = callLimit.timeUnit();
        // 获取异常信息
        Class<? extends Throwable> clazz = callLimit.onLimitException();

        String userKey = null;
        try {
            userKey = userInfoSupport.currentUserKey();
        } catch (Exception e) {
            logger.error("com.mm.call.limit.intf.UserInfoSupport.currentUserKey 获取当前用户信息失败！", e);
        }
        if (null == userKey) {
            throw new IllegalArgumentException("无法获取当前用户信息，请实现com.mm.call.limit.intf.UserInfoSupport接口");
        }
        boolean isAllow = callLimitService.allowCall(methodName, userInfoSupport.currentUserKey(), type, time, timeUnit);

        if (isAllow) {
            try {
                throw clazz.getConstructor(String.class).newInstance("Call is restricted. Please try again later!");
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                logger.error("--- 限流工具CallLimit初始化异常信息出错", e);
            }
        }

    }

}

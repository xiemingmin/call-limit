package top.xiemingmin.call.limit.handler;

import top.xiemingmin.call.limit.annotation.CallLimit;
import top.xiemingmin.call.limit.enums.LimitTypeEnum;
import top.xiemingmin.call.limit.intf.UserInfoSupport;
import top.xiemingmin.call.limit.service.CallLimitService;
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
    public void doBefore(JoinPoint joinPoint, CallLimit callLimit) throws Throwable{
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
            logger.error("UserInfoSupport.currentUserKey 获取当前用户信息失败！", e);
        }
        if (null == userKey) {
            throw new IllegalArgumentException("无法获取当前用户信息，请实现com.xiemingmin.call.limit.intf.UserInfoSupport接口");
        }
        boolean isAllow = callLimitService.allowCall(methodName, userInfoSupport.currentUserKey(), type, time, timeUnit);

        if (!isAllow) {
            try {
                throw clazz.getConstructor(String.class).newInstance("Call is restricted. Please try again later!");
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                logger.error("--- 限流工具CallLimit初始化异常信息出错", e);
            }
        }

    }

}

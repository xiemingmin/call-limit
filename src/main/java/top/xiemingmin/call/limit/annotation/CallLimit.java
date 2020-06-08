package top.xiemingmin.call.limit.annotation;

import top.xiemingmin.call.limit.enums.LimitTypeEnum;

import java.lang.annotation.*;
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
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CallLimit {

    /**
     * 限制类型，单机or全局
     */
    LimitTypeEnum type() default LimitTypeEnum.One_machine;

    /**
     * 限制时间，单位时间只能调用一次
     */
    long time() default 1000L;

    /**
     * 时间单位
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

    /**
     * 触发限制抛出的异常类型
     */
    Class<? extends Throwable> onLimitException() default RuntimeException.class;

}

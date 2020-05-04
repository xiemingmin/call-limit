package com.mm.call.limit.annotates;

import com.mm.call.limit.enums.LimitTypeEnum;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: xiemingmin
 * @Description: 接口防刷工具
 * @Date: 2020/4/29 22:09
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

# java轻量级接口限流/防刷插件

### 简介
call-limit提供接口限流、防刷的功能，插件基于spring开发，在应用应用的任何一个逻辑层皆可使用（web、service、dao），
插件支持单机应用下的限流和分布式应用的限流（分布式应用限流需要依赖redis），在简单业务场景下插件可为大家提供轻量
无逻辑侵入的限流、防刷的支持。

### maven坐标

```
<dependency>
    <groupId>top.xiemingmin</groupId>
    <artifactId>call-limit</artifactId>
    <version>1.0.1-SNAPSHOT</version>
</dependency>
```

### 用法

1. 在项目中添加插件maven依赖
2. 在spring xml配置中配置相关的bean 

* 单机场景：
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:mm="http://www.xiemingmin.top/schema/mm"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.xiemingmin.top/schema/mm
       http://www.xiemingmin.top/schema/mm.xsd">

    <mm:callLimit/>
</beans>
```

* 分布式场景：
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:mm="http://www.xiemingmin.top/schema/mm"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.xiemingmin.top/schema/mm
       http://www.xiemingmin.top/schema/mm.xsd">

    <bean id="poolConfig" class="redis.clients.jedis.JedisPoolConfig">
        <!-- 资源池中的最大连接数 -->
        <property name="maxTotal" value="${redis.maxTotal}" />
        <!-- 资源池允许的最大空闲连接数 -->
        <property name="maxIdle" value="${redis.maxIdle}"/>
        <!-- 资源池确保的最少空闲连接数 -->
        <property name="minIdle" value="${redis.minIdle}" />
        <!-- 向资源池借用连接时是否做连接有效性检测（ping）。检测到的无效连接将会被移除 -->
        <property name="testOnBorrow" value="${redis.testOnBorrow}" />
        <!-- 向资源池归还连接时是否做连接有效性检测（ping）。检测到无效连接将会被移除 -->
        <property name="testOnReturn" value="${redis.testOnReturn}" />
    </bean>

    <bean id="jedisPool" class="redis.clients.jedis.JedisPool">
        <constructor-arg ref="poolConfig" />
        <constructor-arg value="${redis.host}" />
        <constructor-arg type="int" value="${redis.port}" />
        <constructor-arg type="int" value="${redis.timeout}" />
        <constructor-arg value="${redis.password}" />
    </bean>

    <mm:callLimit jedisPool-ref="jedisPool"/>
</beans>
```
3.实现用户信息接口（`top.xiemingmin.call.limit.intf.UserInfoSupport`）

返回每个请求线程的用户唯一标识，可使用请求的客户端ip，插件中会调用次接口判断是否同一用户的请求。

ps：可将用户信息放入ThreadLocal在此方法中直接取值
```java
package com.xiemingmin.service;

import org.springframework.stereotype.Service;
import top.xiemingmin.call.limit.intf.UserInfoSupport;

@Service
public class UserInfoServiceImpl implements UserInfoSupport {
    @Override
    public String currentUserKey() {
        return LoginContext.getUserId();
    }
}
```
4.在需要限流或防刷的方法上添加`top.xiemingmin.call.limit.annotation.CallLimit`注解

参数说明：

- time：单位时间内值允许调用1次
- timeUnit：时间单位

如下配置表示30秒内值允许调用一次

```
@GetMapping("/getPerson/{name}")
@CallLimit(time = 30, timeUnit = TimeUnit.SECONDS)
public Result<List<PersonDo>> getPerson(@PathVariable String name){
    return personService.findPersonByName(name);
}
```

### 效果

第一次调用（成功）
![调用成功](https://img-blog.csdnimg.cn/20200529232358107.png)

第二次调用（被限制）
![被限制](https://img-blog.csdnimg.cn/20200529013540174.png)

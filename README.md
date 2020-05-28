## 接口限流插件

### 简介
call-limit提供接口限流、放刷的功能，插件基于spring开发，在应用应用的任何一个逻辑层皆可使用（web、service、dao），
插件支持单机应用下的限流和分布式应用的限流（分布式应用限流需要依赖redis），在简单业务场景下插件可为大家提供轻量
无逻辑侵入的限流、放刷的支持。

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



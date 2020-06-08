package top.xiemingmin.call.limit.config.parser;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import top.xiemingmin.call.limit.client.impl.RedisClientImpl;
import top.xiemingmin.call.limit.service.impl.CallLimitServiceImpl;

/**
 * *******************************************************************************************
 * ***            X I E M I N G M I N . T O P  ---  U N C L E  G E O R G E                 ***
 * *******************************************************************************************
 *
 * @author xiemingmin
 * Date: 2020/4/29 22:09
 * Description: 接口防刷工具
 */
public class CallLimitBeanDefinitionParser implements BeanDefinitionParser {

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        String jedisPoolRef = element.getAttribute("jedisPool-ref");
        // 向spring容器中初始化CallLimitServiceImpl
        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(CallLimitServiceImpl.class);
        beanDefinition.setAutowireMode(AutowireCapableBeanFactory.AUTOWIRE_BY_NAME);
        if (jedisPoolRef != null && jedisPoolRef != ""){
            // 向spring容器中初始化RedisClientImpl
            RootBeanDefinition redisClientBeanDefinition = new RootBeanDefinition(RedisClientImpl.class);
            redisClientBeanDefinition.setAutowireMode(AutowireCapableBeanFactory.AUTOWIRE_BY_NAME);
            redisClientBeanDefinition.getPropertyValues().addPropertyValue("jedisPool", new RuntimeBeanReference(jedisPoolRef));
            // 属性注入是会做一次初始化，所以此处不需要向容器中注册
//        parserContext.getRegistry().registerBeanDefinition("redisClientImpl", redisClientBeanDefinition);
            beanDefinition.getPropertyValues().addPropertyValue("redisClient", redisClientBeanDefinition);
        }
        parserContext.getRegistry().registerBeanDefinition("callLimitServiceImpl", beanDefinition);

        return beanDefinition;
    }
}

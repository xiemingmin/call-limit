package top.xiemingmin.call.limit.config.parser;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import top.xiemingmin.call.limit.client.impl.RedisClientImpl;
import top.xiemingmin.call.limit.service.impl.CallLimitServiceImpl;

/**
 * @Auther: xiemingmin
 * @Description: callLimit beanDefinitionParser
 * @Date: 2020/5/4 22:44
 */
public class CallLimitBeanDefinitionParser implements BeanDefinitionParser {

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        // 向spring容器中初始化RedisClientImpl
        RootBeanDefinition redisClientBeanDefinition = new RootBeanDefinition(RedisClientImpl.class);
        redisClientBeanDefinition.setAutowireMode(AutowireCapableBeanFactory.AUTOWIRE_BY_NAME);
        String redisServeIp = element.getAttribute("redisServeIp");
        redisClientBeanDefinition.getPropertyValues().addPropertyValue("redisServeIp", redisServeIp);
        String redisServePort = element.getAttribute("redisServePort");
        redisClientBeanDefinition.getPropertyValues().addPropertyValue("redisServePort", redisServePort);
        String authPwd = element.getAttribute("authPwd");
        redisClientBeanDefinition.getPropertyValues().addPropertyValue("auth", authPwd);
        parserContext.getRegistry().registerBeanDefinition("redisClientImpl", redisClientBeanDefinition);
        // 向spring容器中初始化CallLimitServiceImpl
        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(CallLimitServiceImpl.class);
        redisClientBeanDefinition.setAutowireMode(AutowireCapableBeanFactory.AUTOWIRE_BY_NAME);
        beanDefinition.getPropertyValues().addPropertyValue("redisClient", redisClientBeanDefinition);
        parserContext.getRegistry().registerBeanDefinition("callLimitServiceImpl", beanDefinition);

        return beanDefinition;
    }
}

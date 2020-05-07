package top.xiemingmin.call.limit.config.parser;

import top.xiemingmin.call.limit.service.impl.CallLimitServiceImpl;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

/**
 * @Auther: xiemingmin
 * @Description: callLimit beanDefinitionParser
 * @Date: 2020/5/4 22:44
 */
public class CallLimitBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {


    protected Class getBeanClass(Element element) {
        return CallLimitServiceImpl.class;
    }

    protected void doParse(Element element, BeanDefinitionBuilder beanBuilder) {
        String redisUrl = element.getAttribute("redisUrl");
        beanBuilder.setAutowireMode(AutowireCapableBeanFactory.AUTOWIRE_BY_NAME);
        beanBuilder.addPropertyValue("redisUrl", redisUrl);
//        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(CallLimitAnnotationHandler.class);
//        beanDefinitionBuilder.getBeanDefinition();
    }

}

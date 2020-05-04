package com.mm.call.limit.config.parser;

import com.mm.call.limit.service.impl.CallLimitServiceImpl;
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

    protected void doParse(Element element, BeanDefinitionBuilder bean) {
        String redisUrl = element.getAttribute("redisUrl");
        bean.addPropertyValue("redisUrl", redisUrl);
    }

}

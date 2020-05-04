package com.mm.call.limit.config;

import com.mm.call.limit.config.parser.CallLimitBeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @Auther: xiemingmin
 * @Description: schema处理器
 * @Date: 2020/5/4 22:33
 */
public class MmNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("callLimit", new CallLimitBeanDefinitionParser());
    }
}

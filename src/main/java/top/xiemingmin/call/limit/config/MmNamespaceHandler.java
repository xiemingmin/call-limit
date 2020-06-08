package top.xiemingmin.call.limit.config;

import top.xiemingmin.call.limit.config.parser.CallLimitBeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * *******************************************************************************************
 * ***            X I E M I N G M I N . T O P  ---  U N C L E  G E O R G E                 ***
 * *******************************************************************************************
 *
 * @author xiemingmin
 * Date: 2020/4/29 22:09
 * Description: 接口防刷工具
 */
public class MmNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("callLimit", new CallLimitBeanDefinitionParser());
    }
}

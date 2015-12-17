/*
 * 描          述:  <描述>
 * 修  改   人:  Rain.he
 * 修改时间:  2015年11月12日
 * <修改描述:>
 */
package com.tx.component.messagerouter.context;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.tx.component.messagerouter.exception.MRSException;

/**
 * 消息路由服务配置容器
 * 
 * @author Rain.he
 * @version [版本号, 2015年5月21日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class MRSContextConfigurator implements InitializingBean, ApplicationContextAware, BeanNameAware {
    
    /** 日志 */
    protected static final Logger logger = LoggerFactory.getLogger(MRSContext.class);
    
    /** springContext */
    protected static ApplicationContext applicationContext;
    
    /** 本身Bean名称 */
    protected static String beanName;
    
    /** 请求器名称和接收器映射 */
    protected Map<Class<? extends MRSRequest>, MRSReceiver<? extends MRSRequest, ? extends MRSResponse>> request2Receiver = new HashMap<>();
    
    @SuppressWarnings({ "rawtypes" })
    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("消息路由服务 配置容器启动...");
        
        // 加载消息路由服务接收器
        Map<String, MRSReceiver> receivers = applicationContext.getBeansOfType(MRSReceiver.class);
        if (MapUtils.isNotEmpty(receivers)) {
            for (MRSReceiver<?, ?> receiver : receivers.values()) {
                Class<? extends MRSRequest> requestType = receiver.getRequestType();
                if (this.request2Receiver.containsKey(requestType)) {
                    throw new MRSException("存在相同的消息路由服务 : {}", receiver.getClass().getName());
                }
                this.request2Receiver.put(requestType, receiver);
                logger.info("加载消息路由服务 : {}[{}]", receiver.getClass().getName(), requestType.getName());
            }
        }
        
        logger.info("消息路由服务 配置容器启动完毕...");
    }
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        MRSContextConfigurator.applicationContext = applicationContext;
    }
    
    @Override
    public void setBeanName(String beanName) {
        MRSContextConfigurator.beanName = beanName;
    }
}

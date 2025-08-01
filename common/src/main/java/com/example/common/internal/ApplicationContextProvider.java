package com.example.common.internal;

import org.springframework.beans.BeansException;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component  // Bean 등록
@ComponentScan(basePackages = "com.example")    // com.example 패키지의 래 모든 클래스의 Bean을 스캔
@ConfigurationPropertiesScan(basePackages = "com.example")  // com.example 패키지에서 @ConfigurationProperties 스캔
public class ApplicationContextProvider implements ApplicationContextAware {

    /**
     * ApplicationContext를 저장하기 위한 holder inner class
     */
    private static final class ApplicationContextHolder{
        private static final InnerApplicationContextProvider CONTEXT_PROVIDER = new InnerApplicationContextProvider();

        private ApplicationContextHolder(){}
    }

    /**
     * ApplicationContext instance를 제공하기 위한 inner class
     */
    private static final class InnerApplicationContextProvider{
        private ApplicationContext applicationContext;

        private InnerApplicationContextProvider(){}

        private void setApplicationContext(ApplicationContext ctx){this.applicationContext = ctx;}

        private ApplicationContext getContext(){return applicationContext;}
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException{
        ApplicationContextHolder.CONTEXT_PROVIDER.setApplicationContext(ctx);
    }

    public static ApplicationContext getContext(){return ApplicationContextHolder.CONTEXT_PROVIDER.getContext();}

}

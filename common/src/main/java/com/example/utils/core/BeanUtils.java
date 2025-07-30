package com.example.utils.core;

import org.springframework.context.ApplicationContext;

public class BeanUtils {

    private BeanUtils(){}

    /**
     * container에서 해당 classType의 bean object 조회.
     * <pre>
     *     ClientConfiguration clientConfiguration =
     *          BeanUtils.getBean(ClientConfiguration.class);
     * </pre>
     *
     * @param classType     조회 대상 class type
     * @return              조회된 bean object
     */
    public static <T> T getBean(Class<T> classType){
        ApplicationContext applicationContext = F
    }
}

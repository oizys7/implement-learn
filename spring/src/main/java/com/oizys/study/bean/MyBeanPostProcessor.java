package com.oizys.study.bean;

import com.oizys.study.annotation.Component;

/**
 * @author wyn
 * Created on 2025/8/5
 */
@Component
public class MyBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object afterInitializeBean(Object bean, String beanName) {
        System.out.println("MyBeanPostProcessor "  + bean + " 初始化完成");
        return bean;
    }
}

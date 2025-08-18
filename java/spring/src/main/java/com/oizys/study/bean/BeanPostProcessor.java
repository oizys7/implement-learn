package com.oizys.study.bean;

/**
 * @author wyn
 * Created on 2025/8/5
 */
public interface BeanPostProcessor {
    default Object beforeInitializeBean(Object bean, String beanName) {
        return bean;
    }

    default Object afterInitializeBean(Object bean, String beanName) {
        return bean;
    }
}

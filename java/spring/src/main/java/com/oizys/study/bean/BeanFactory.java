package com.oizys.study.bean;

import com.oizys.study.BeansException;

import java.util.List;

/**
 * @author wyn
 * Created on 2025/8/5
 */
public interface BeanFactory {
    /**
     * 获取bean
     *
     * @param name
     * @return
     * @throws BeansException bean不存在时
     */
    Object getBean(String name) throws BeansException;

    /**
     * 根据名称和类型查找bean
     *
     * @param requiredType
     * @param <T>
     * @return
     * @throws BeansException
     */
    <T> T getBean(Class<T> requiredType) throws BeansException;

    <T> List<T> getBeans(Class<T> requiredType) throws BeansException;

//    boolean containsBean(String value);
}

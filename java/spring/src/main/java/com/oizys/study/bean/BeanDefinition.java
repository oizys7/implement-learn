package com.oizys.study.bean;

import com.oizys.study.annotation.AutoWrite;
import com.oizys.study.annotation.Component;
import com.oizys.study.annotation.PostConstruct;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * @author wyn
 * Created on 2025/8/5
 */
public class BeanDefinition {

    private final String name;
    private final Constructor<?> noArgsConstructor;
    private final Method postConstructMethod;
    private final List<Field> autowiredFields;
    private final Class<?> beanType;

    public BeanDefinition(Class<?> beanType) {
        this.beanType = beanType;
        Component component = beanType.getDeclaredAnnotation(Component.class);
        this.name = component.name().isEmpty() ? beanType.getSimpleName() : component.name();
        try {
            this.noArgsConstructor = beanType.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        this.postConstructMethod = Arrays.stream(beanType.getMethods())
                .filter(method -> method.isAnnotationPresent(PostConstruct.class))
                .findAny()
                .orElse(null);
        this.autowiredFields = Arrays.stream(beanType.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(AutoWrite.class))
                .toList();
    }

    public String getName() {
        return this.name;
    }

    public Constructor<?> getNoArgsConstructor() {
        return this.noArgsConstructor;
    };


    public Method getPostConstructMethod() {
        return postConstructMethod;
    }

    public List<Field> getAutowiredFields() {
        return autowiredFields;
    }

    public Class<?> getBeanType() {
        return beanType;
    }
}

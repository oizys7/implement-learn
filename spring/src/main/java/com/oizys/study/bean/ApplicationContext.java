package com.oizys.study.bean;

import com.oizys.study.BeansException;
import com.oizys.study.annotation.Component;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wyn
 * Created on 2025/8/5
 */
public class ApplicationContext implements BeanFactory {

    private final Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

    private final Map<String, Object> ioc = new HashMap<>();
    private final Map<String, Object> loadingIoc = new HashMap<>();
    private final List<BeanPostProcessor> postProcessors = new ArrayList<>();

    public ApplicationContext(String packageName) throws Exception {
        scanPackage(packageName).stream().filter(this::canCreate).forEach(this::createBeanDefinition);
        initBeanPostProcessor();
        beanDefinitionMap.values().forEach(this::createBean);
    }

    protected Object createBean(BeanDefinition beanDefinition) {
        String name = beanDefinition.getName();
        if (ioc.containsKey(name)) {
            return ioc.get(name);
        }
        if (loadingIoc.containsKey(name)) {
            return loadingIoc.get(name);
        }
        return doCreateBean(beanDefinition);
    }

    private Object doCreateBean(BeanDefinition beanDefinition) {
        Constructor<?> constructor = beanDefinition.getNoArgsConstructor();
        Object bean = null;
        try {
            bean = constructor.newInstance();
            loadingIoc.put(beanDefinition.getName(), bean);
            autowiredBean(bean, beanDefinition);
            bean = initializeBean(bean, beanDefinition);
            loadingIoc.remove(beanDefinition.getName());
            ioc.put(beanDefinition.getName(), bean);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return bean;

    }

    private Object initializeBean(Object bean, BeanDefinition beanDefinition) throws Exception {
        for (BeanPostProcessor postProcessor : postProcessors) {
            bean = postProcessor.beforeInitializeBean(bean, beanDefinition.getName());
        }

        Method postConstructMethod = beanDefinition.getPostConstructMethod();
        if (postConstructMethod != null) {
            postConstructMethod.invoke(bean);
        }

        for (BeanPostProcessor postProcessor : postProcessors) {
            bean = postProcessor.afterInitializeBean(bean, beanDefinition.getName());
        }
        return bean;
    }

    private void autowiredBean(Object bean, BeanDefinition beanDefinition) throws IllegalAccessException {
        for (Field field : beanDefinition.getAutowiredFields()) {
            field.setAccessible(true);
            field.set(bean, getBean(field.getType()));
        }
    }

    private void initBeanPostProcessor() {
        beanDefinitionMap.values().stream()
                .filter(bd -> BeanPostProcessor.class.isAssignableFrom(bd.getBeanType()))
                .map(this::createBean)
                .map(BeanPostProcessor.class::cast)
                .forEach(postProcessors::add);
    }

    private BeanDefinition createBeanDefinition(Class<?> aClass) {
        BeanDefinition beanDefinition = new BeanDefinition(aClass);
        if (beanDefinitionMap.containsKey(beanDefinition.getName())) {
            throw new RuntimeException("bean名字重复");
        }
        beanDefinitionMap.put(beanDefinition.getName(), beanDefinition);
        return beanDefinition;
    }

    protected boolean canCreate(Class<?> type) {
        return type.isAnnotationPresent(Component.class) && !type.isAnnotation();
    }

    public List<Class<?>> scanPackage(String packageName) throws Exception {
        List<Class<?>> classList = new ArrayList<>();
        URL resource = this.getClass().getClassLoader().getResource(packageName.replace(".", File.separator));
        Path path = Path.of(resource.toURI());
        Files.walkFileTree(path, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Path absolutePath = file.toAbsolutePath();
                if (absolutePath.toString().endsWith(".class")) {
                    String clazzStr = absolutePath.toString().replace(File.separator, ".");
                    int packageIndex = clazzStr.indexOf(packageName);
                    String className = clazzStr.substring(packageIndex, clazzStr.length() - ".class".length());
                    try {
                        classList.add(Class.forName(className));
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
                return FileVisitResult.CONTINUE;
            }
        });
        return classList;
    }

    @Override
    public Object getBean(String name) throws BeansException {
        if (name == null) {
            return null;
        }
        Object bean = ioc.get(name);
        if (bean != null) {
            return bean;
        }
        if (beanDefinitionMap.containsKey(name)) {
            return createBean(beanDefinitionMap.get(name));
        }
        return null;
    }

    @Override
    public <T> T getBean(Class<T> beanType) throws BeansException {
        String beanName = this.beanDefinitionMap.values().stream()
                .filter(bd -> beanType.isAssignableFrom(bd.getBeanType()))
                .map(BeanDefinition::getName)
                .findFirst()
                .orElse(null);
        return (T) getBean(beanName);
    }

    @Override
    public <T> List<T> getBeans(Class<T> beanType) {
        return this.beanDefinitionMap.values().stream()
                .filter(bd -> beanType.isAssignableFrom(bd.getBeanType()))
                .map(BeanDefinition::getName)
                .map(this::getBean)
                .map((bean) -> (T) bean)
                .toList();
    }
}

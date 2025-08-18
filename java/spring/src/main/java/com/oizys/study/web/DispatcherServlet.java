package com.oizys.study.web;

import com.alibaba.fastjson2.JSONObject;
import com.oizys.study.annotation.Component;
import com.oizys.study.annotation.Controller;
import com.oizys.study.annotation.Param;
import com.oizys.study.annotation.RequestMapping;
import com.oizys.study.bean.BeanPostProcessor;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 调度程序servlet
 *
 * @author wyn
 * Created on 2025/8/15@date 2025/08/15
 */
@Component
public class DispatcherServlet extends HttpServlet implements BeanPostProcessor {
    Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private Map<String, WebHandler> handlerMap = new HashMap<>();
    @Override
    public Object afterInitializeBean(Object bean, String beanName) {
        Class<?> aClass = bean.getClass();
        if (aClass.isAnnotationPresent(Controller.class)) {
            RequestMapping requestMapping = aClass.getAnnotation(RequestMapping.class);
            String classUrl = requestMapping.path() != null ? requestMapping.path() : "";
            Arrays.stream(aClass.getDeclaredMethods()).forEach(method -> {
                RequestMapping methodRm = method.getAnnotation(RequestMapping.class);
                if (methodRm != null) {
                    String key = classUrl.concat(methodRm.path());
                    WebHandler webHandler = new WebHandler(bean, method);
                    if (handlerMap.put(key, webHandler) != null) {
                        throw new RuntimeException("controller定义重复：" + key);
                    }
                }
            });
        }
        return bean;
    }
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebHandler handler = handlerMap.get(req.getRequestURI());
        if (handler == null) {
            handler404(req, resp);
            return;
        }
        try {
            Object controllerBean = handler.getControllerBean();
            Object[] args = resolveArgs(req, handler.getMethod());
            Object result = handler.getMethod().invoke(controllerBean, args);
            switch (handler.getResultType()) {
                case HTML -> {
                    resp.setContentType("text/html;charset=UTF-8");
                    resp.getWriter().write(result.toString());
                }
                case JSON -> {
                    resp.setContentType("application/json;charset=UTF-8");
                    resp.getWriter().write(JSONObject.toJSONString(result));
                }
//                case LOCAL -> {
//                    ModelAndView mv = (ModelAndView) result;
//                    String view = mv.getView();
//                    InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(view);
//                    try (resourceAsStream) {
//                        String html = new String(resourceAsStream.readAllBytes());
//                        html = renderTemplate(html, mv.getContext());
//                        resp.setContentType("text/html;charset=UTF-8");
//                        resp.getWriter().write(html);
//                    }
//                }
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private Object[] resolveArgs(HttpServletRequest req, Method method) {
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            String value = null;
            Param param = parameter.getAnnotation(Param.class);
            if (param != null) {
                value = req.getParameter(param.value());
            } else {
                value = req.getParameter(parameter.getName());
            }
            Class<?> parameterType = parameter.getType();
            if (String.class.isAssignableFrom(parameterType)) {
                args[i] = value;
            } else if (Integer.class.isAssignableFrom(parameterType)) {
                args[i] = Integer.parseInt(value);
            } else {
                args[i] = null;
            }
        }
        try {
            Collection<Part> parts = req.getParts();
            System.out.println( parts);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
        return args;
    }

    private void handler404(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

}

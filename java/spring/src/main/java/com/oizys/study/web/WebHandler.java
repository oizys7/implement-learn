package com.oizys.study.web;

import com.oizys.study.annotation.ResponseBody;

import java.lang.reflect.Method;

/**
 * @author wyn
 * Created on 2025/8/15
 */
public class WebHandler {
    private final Object controllerBean;

    private final Method method;

    private final ResultType resultType;

    public WebHandler(Object controllerBean, Method method) {
        this.controllerBean = controllerBean;
        this.method = method;
        this.resultType = resovleResultType(controllerBean, method);
    }

    private ResultType resovleResultType(Object controllerBean, Method method) {
        if (method.isAnnotationPresent(ResponseBody.class)) {
            return ResultType.JSON;
        }
//        if (method.getReturnType() == ModelAndView.class) {
//            return ResultType.LOCAL;
//        }
        return ResultType.HTML;
    }

    public Object getControllerBean() {
        return controllerBean;
    }

    public Method getMethod() {
        return method;
    }

    public ResultType getResultType() {
        return resultType;
    }


    enum ResultType {
        JSON, HTML, LOCAL
    }
}

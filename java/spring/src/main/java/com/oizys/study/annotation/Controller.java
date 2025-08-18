package com.oizys.study.annotation;

import java.lang.annotation.*;

/**
 * @author wyn
 * Created on 2025/8/4
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Controller {
}

package com.oizys.study;

/**
 * @author wyn
 * Created on 2025/8/5
 */
public class BeansException extends RuntimeException {

    public BeansException(String msg) {
        super(msg);
    }

    public BeansException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

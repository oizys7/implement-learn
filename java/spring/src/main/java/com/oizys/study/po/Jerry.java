package com.oizys.study.po;

import com.oizys.study.annotation.AutoWrite;
import com.oizys.study.annotation.Component;
import com.oizys.study.annotation.PostConstruct;

/**
 * @author wyn
 * Created on 2025/8/5
 */
@Component
public class Jerry {
    @AutoWrite
    private Tom tom;

    public Jerry() {
        System.out.println("Jerry 创建");
    }

    @PostConstruct
    public void post() {
        System.out.println("Jerry PostConstruct，有一个属性" + tom);
    }
}

package com.oizys.study.po;

import com.oizys.study.annotation.AutoWrite;
import com.oizys.study.annotation.Component;
import com.oizys.study.annotation.PostConstruct;

/**
 * @author wyn
 * Created on 2025/8/5
 */
@Component
public class Tom {
    @AutoWrite
    private Jerry jerry;

    public Tom() {
        System.out.println("Tom init");
    }

    @PostConstruct
    public void post() {
        System.out.println("Tom PostConstruct，有一个属性" + jerry);
    }
}

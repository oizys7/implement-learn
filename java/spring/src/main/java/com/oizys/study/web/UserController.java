package com.oizys.study.web;

import com.oizys.study.annotation.*;
import com.oizys.study.po.User;

/**
 * @author wyn
 * Created on 2025/8/15
 */
@Controller
@Component
@RequestMapping(path = "/user")
public class UserController {
    @PostConstruct
    public void init() {
        System.out.println("UserController init()");
    }

    @RequestMapping(path = "/hello")
    public String hello(@Param("word") String word) {
        return "hello" + word;
    }

    @RequestMapping(path = "/user")
    @ResponseBody
    public User user() {
        return new User("zhangsan", 18);
    }

}

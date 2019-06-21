package com.yhau.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

/**
 * Created by nowcoder on 2016/7/10.
 */
@Controller
public class SettingController {

    @RequestMapping("/setting")
    public String setting(HttpSession httpSession) {
        return "/demo.html";
    }
}

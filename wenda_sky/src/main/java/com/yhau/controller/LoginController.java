package com.yhau.controller;

import com.yhau.core.commom.exception.ExceptionEnum;
import com.yhau.core.util.ResponseUtil;
import com.yhau.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserService userService;

    @RequestMapping(path = {"/reg"}, method = {RequestMethod.POST})
    public String reg(Model model,
                      @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value = "next", required = false) String next,
                      HttpServletResponse response) {
        Map<String, String> map = userService.register(username, password);
        try {
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket"));
                cookie.setPath("/");
                response.addCookie(cookie);
                if (StringUtils.isBlank(next)) {
                    return "redirect:/" + next;
                }
                return "redirect:/";
            } else {
                model.addAttribute("msg", map.get("msg"));
                return "/login.html";
            }

        } catch (Exception e) {
            logger.error("注册异常" + e.getMessage());
            return "/login.html";
        }
    }

    @RequestMapping("/logout")
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/";
    }

    @RequestMapping(value = ("/login"), method = {RequestMethod.POST})
    public String login(Model model,
                        @RequestParam("jsEmailIpt") String username,
                        @RequestParam("jsPasswordIpt") String password,
                        @RequestParam(value = "next", required = false) String next,
                        @RequestParam(value = "jsRemLoginChk", defaultValue = "false") boolean rememberme,
                        HttpServletResponse response) {
        try {
            Map<String, String> map = userService.login(username, password);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket"));
                cookie.setPath("/");
                if (rememberme) {
                    cookie.setMaxAge(3600 * 24 * 5);
                }
                response.addCookie(cookie);
                if (!StringUtils.isBlank(next) && !next.equals("")) {
                    return "redirect:" + next;
                }
                return "redirect:/";
            } else {
                model.addAttribute("msg", map.get("msg"));
                return "/login.html";
            }
        } catch (Exception e) {
            logger.error("登录异常" + e.getMessage());
            return "/login.html";
        }
    }

    @RequestMapping(value = ("/reglogin"), method = {RequestMethod.GET})
    public String regloginPage(Model model, @RequestParam(value = "next", required = false, defaultValue = "") String next) {
        model.addAttribute("next", next);
        return "/login.html";
    }

    @RequestMapping(value = "/login", method = {RequestMethod.GET})
    public String loin(Model model) {
        model.addAttribute("isLoginOrRegister", true);
        model.addAttribute("tabLogin", "tab-selected");
        model.addAttribute("register", "register");
        return "/demo.html";
    }

    @RequestMapping(value = "/register", method = {RequestMethod.GET})
    public String register(Model model) {
        model.addAttribute("isLoginOrRegister", false);
        model.addAttribute("tabReg", "tab-selected");
        model.addAttribute("login", "login");
        return "/demo.html";
    }

    @RequestMapping(value = "/sendCaptcha", method = {RequestMethod.POST})
    @ResponseBody
    public String sendCaptcha(@RequestParam("jsEmailIpt") String jsEmailIpt) {
        try {
            return userService.sendCaptcha(jsEmailIpt);
        } catch (Exception e) {
            logger.error("邮箱验证异常" + e.getMessage());
            ExceptionEnum serverError = ExceptionEnum.SERVER_ERROR;
            return ResponseUtil.getJSONString(serverError.getCode(), serverError.getMessage());
        }
    }

    @RequestMapping("/vcodeOvertime")
    public String vcodeOvertime(@RequestParam("jsEmailIpt") String jsEmailIpt) {
        try {
            return userService.vcodeOvertime(jsEmailIpt);
        } catch (Exception e) {
            logger.error("邮箱验证异常" + e.getMessage());
            ExceptionEnum serverError = ExceptionEnum.SERVER_ERROR;
            return ResponseUtil.getJSONString(serverError.getCode(), serverError.getMessage());
        }
    }
}

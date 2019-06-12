package com.yhau.controller;

import com.alibaba.fastjson.JSONObject;
import com.yhau.core.util.ResponseUtil;
import com.yhau.model.Question;
import com.yhau.service.QuestionService;
import com.yhau.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class QuestionController {
    @Resource
    private QuestionService questionService;

    @Resource
    private UserService userService;

    @RequestMapping(value = "/question/add")
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title, @RequestParam("content") String content) {
        JSONObject jsonObject = new JSONObject();
        try {
            ResponseUtil responseUtil = questionService.addQuestion(title, content);
            return jsonObject.toJSONString(responseUtil);
        } catch (Exception e) {
            return jsonObject.toJSONString(ResponseUtil.fail("添加问题失败"));
        }
    }

    @RequestMapping(value = "/question/{qid}")
    public String questionDetail(Model model, @PathVariable("qid") int qid) {
        Question question = questionService.selectById(qid);
        model.addAttribute("question", question);
        model.addAttribute(("user"), userService.getUser(question.getUserId()));
        return "detail.html";
    }
}

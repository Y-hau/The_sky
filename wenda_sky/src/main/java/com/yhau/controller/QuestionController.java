package com.yhau.controller;

import com.alibaba.fastjson.JSONObject;
import com.yhau.config.web.HostHandler;
import com.yhau.core.util.ResponseUtil;
import com.yhau.service.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class QuestionController {
    @Resource
    private QuestionService questionService;
    @Resource
    private HostHandler hostHandler;

    @RequestMapping(value = "/question_add")
    public String add_Que(){
        return "/popup/add_question.html";
    }

    @RequestMapping(value = "/question/add")
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title, @RequestParam("content") String content){
        JSONObject jsonObject = new JSONObject();
        try{
            ResponseUtil responseUtil = questionService.addQuestion(title, content);
            return jsonObject.toJSONString(responseUtil);
        }catch (Exception e){
            return jsonObject.toJSONString(ResponseUtil.fail("添加问题失败"));
        }
    }
}

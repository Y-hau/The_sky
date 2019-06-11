package com.yhau.controller;

import com.alibaba.fastjson.JSONObject;
import com.yhau.core.util.ResponseUtil;
import com.yhau.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MessageController {

    @Autowired
    private MessageService messageService;

    @RequestMapping(value = "/msg/addMessage", method = {RequestMethod.POST})
    public String addMessage(@RequestParam("toName") String toName, @RequestParam("content") String content){
        JSONObject jsonObject = new JSONObject();
        try{
            ResponseUtil responseUtil = messageService.addMessage(toName, content);
            return jsonObject.toJSONString(responseUtil);
        }catch (Exception e){
            return jsonObject.toJSONString(ResponseUtil.fail("添加问题失败"));
        }
    }
}

package com.yhau.controller;

import com.alibaba.fastjson.JSONObject;
import com.yhau.core.util.ResponseUtil;
import com.yhau.model.User;
import com.yhau.service.MessageService;
import com.yhau.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;

    @RequestMapping("/message_add")
    public String addMsg() {
        return "/message/add_message.html";
    }

    @RequestMapping(value = "/msg/addMessage", method = {RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("toName") String toName, @RequestParam("content") String content) {
        JSONObject jsonObject = new JSONObject();
        try {
            ResponseUtil responseUtil = messageService.addMessage(toName, content);
            return jsonObject.toJSONString(responseUtil);
        } catch (Exception e) {
            logger.error("发送信息失败");
            return jsonObject.toJSONString(ResponseUtil.fail("添加问题失败"));
        }
    }

    @RequestMapping(value = "/msg/list", method = RequestMethod.GET)
    public String getConversationList() {
        return "/letter.html";
    }

    @RequestMapping(value = "/msg/detail", method = RequestMethod.GET)
    public String getConversationDetail(Model model, @RequestParam("conversationId") int conversationId) {
        return "/letterDetail.html";
    }

    @RequestMapping(value = "/user/search")
    @ResponseBody
    public String usersjson() {
        List<User> userNameList = userService.getUserNameList();
        JSONObject jsonObject = new JSONObject();
        return jsonObject.toJSONString();
    }
}

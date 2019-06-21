package com.yhau.core.async.handler;

import com.yhau.core.async.EventHandler;
import com.yhau.core.async.EventModel;
import com.yhau.core.async.EventType;
import com.yhau.core.util.MailUtil;

import javax.annotation.Resource;
import java.util.List;

/**
 * 注册验证
 */
public class registerVerifyHandler implements EventHandler {

    @Resource
    private MailUtil mailSender;

    @Override

    public void doHandler(EventModel model) {
        mailSender.sendMail(String.valueOf(model.getActorId()), "邮箱验证", model.getExt("id"));
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return null;
    }
}

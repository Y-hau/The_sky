package com.yhau.service;

import com.yhau.config.web.HostHandler;
import com.yhau.core.util.ResponseUtil;
import com.yhau.dao.MessageDao;
import com.yhau.dao.UserDao;
import com.yhau.model.Message;
import com.yhau.model.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class MessageService {
    @Resource
    private MessageDao messageDao;
    @Resource
    private HostHandler hostHandler;
    @Resource
    private UserDao userDao;

    public ResponseUtil addMessage(String toName, String content){
        User user = userDao.selectByName(toName);
        if(hostHandler.getUser() != null){
            Message message = new Message();
            message.setFromId(hostHandler.getUser().getId());
            message.setToId(user.getId());
            message.setContent(content);
            message.setCreatedDate(new Date());
        }
        return null;
    }
}

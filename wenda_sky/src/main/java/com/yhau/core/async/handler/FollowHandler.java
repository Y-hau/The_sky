package com.yhau.core.async.handler;

import com.yhau.core.async.EventHandler;
import com.yhau.core.async.EventModel;
import com.yhau.core.async.EventType;
import com.yhau.core.util.StaticUtil;
import com.yhau.dao.MessageDao;
import com.yhau.dao.UserDao;
import com.yhau.model.Message;
import com.yhau.model.User;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;


@Component
public class FollowHandler implements EventHandler {
    @Resource
    private UserDao userDao;
    @Resource
    private MessageDao messageDao;

    @Override
    public void doHandler(EventModel model) {
        Message message = new Message();
        message.setFromId(StaticUtil.SYSTEM_USERID);
        message.setToId(model.getEntityOwnerId());
        User user = userDao.selectById(model.getActorId());
        if (model.getEntityType() == StaticUtil.ENTITY_QUESTION) {
            message.setContent("用户" + user.getName()
                    + "关注了你的问题，http://localhost:8080/question/" + model.getEntityId());
        } else if (model.getEntityType() == StaticUtil.ENTITY_USER) {
            message.setContent("用户" + user.getName()
                    + "关注了你,http://127.0.0.1:8080/user/" + model.getActorId());
        }
        messageDao.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.FOLLOW);
    }
}

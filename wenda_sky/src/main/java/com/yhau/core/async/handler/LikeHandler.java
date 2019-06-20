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
import java.util.Date;
import java.util.List;

@Component
public class LikeHandler implements EventHandler {

    @Resource
    MessageDao messageDao;

    @Resource
    UserDao userDao;

    @Override
    public void doHandler(EventModel model) {
        Message message = new Message();
        message.setFromId(StaticUtil.SYSTEM_USERID);
        message.setToId(model.getEntityId());
        message.setCreatedDate(new Date());
        User user = userDao.selectById(model.getActorId());
        message.setContent("用户" + user.getName() +
                "赞了你的评论,http://127.0.0.1:8080/question/" + model.getExt("questionId"));
        messageDao.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}

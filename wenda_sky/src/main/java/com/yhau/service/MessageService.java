package com.yhau.service;

import com.yhau.config.web.HostHandler;
import com.yhau.core.commom.exception.ExceptionEnum;
import com.yhau.core.commom.exception.SkyException;
import com.yhau.core.util.ResponseUtil;
import com.yhau.dao.MessageDao;
import com.yhau.dao.UserDao;
import com.yhau.model.Message;
import com.yhau.model.User;
import com.yhau.model.ViewObject;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MessageService {
    @Resource
    private MessageDao messageDao;
    @Resource
    private HostHandler hostHandler;
    @Resource
    private UserDao userDao;
    @Resource
    private SensitiveService sensitiveService;

    public ResponseUtil addMessage(String toName, String content) {
        User user = userDao.selectByName(toName);
        if (user == null) {
            throw new SkyException(ExceptionEnum.NO_THIS_USER);
        }
        if (hostHandler.getUser() != null) {
            Message message = new Message();
            message.setFromId(hostHandler.getUser().getId());
            message.setToId(user.getId());
            message.setContent(HtmlUtils.htmlEscape(sensitiveService.filter(content)));
            message.setCreatedDate(new Date());
            messageDao.addMessage(message);
        } else {
            return ResponseUtil.ok(999);
        }
        return ResponseUtil.ok();
    }

    public List<ViewObject> getConversationList() {
        int localUserId = hostHandler.getUser().getId();
        List<ViewObject> conversations = new ArrayList<ViewObject>();
        List<Message> conversationList = messageDao.getConversationList(localUserId, 0, 10);
        for (Message msg : conversationList) {
            ViewObject vo = new ViewObject();
            vo.set("conversation", msg);
            int targetId = msg.getFromId() == localUserId ? msg.getToId() : msg.getFromId();
            User user = userDao.selectById(targetId);
            vo.set("user", user);
            vo.set("unread", messageDao.getConvesationUnreadCount(localUserId, msg.getConversationId()));
            conversations.add(vo);
        }
        return conversations;
    }
}

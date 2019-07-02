package com.yhau.service;

import com.yhau.config.web.HostHandler;
import com.yhau.core.async.EventModel;
import com.yhau.core.async.EventProducer;
import com.yhau.core.async.EventType;
import com.yhau.core.util.StaticUtil;
import com.yhau.dao.CommentDao;
import com.yhau.dao.QuestionDao;
import com.yhau.model.Comment;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class CommentService {
    @Resource
    private CommentDao commentDao;
    @Resource
    private HostHandler hostHandler;
    @Resource
    private SensitiveService sensitiveService;
    @Resource
    private QuestionDao questionDao;

    @Resource
    private EventProducer eventProducer;

    public void addComment(int questionId, String content) {
        if (hostHandler != null) {
            Comment comment = new Comment();
            comment.setUserId(hostHandler.getUser().getId());
            comment.setContent(sensitiveService.filter(HtmlUtils.htmlEscape(content)));
            comment.setCreatedDate(new Date());
            comment.setEntityId(questionId);
            comment.setEntityType(StaticUtil.ENTITY_QUESTION);
            comment.setStatus(StaticUtil.status);
            commentDao.addComment(comment);
            int commentCount = commentDao.getCommentCount(comment.getEntityId(), comment.getEntityType());
            questionDao.updateCommentCount(comment.getEntityId(), commentCount);

            eventProducer.fireEvent(new EventModel(EventType.COMMENT).setActorId(comment.getUserId())
                    .setEntityId(questionId));
        }
    }

    public List<Comment> getCommentsByEntity(int entityId, int entityType) {
        return commentDao.getCommentByEntity(entityId, entityType);
    }

    public Comment getCommentById(int commentId) {
        return commentDao.getCommentById(commentId);
    }

    public int getUserCommentCount(int userId) {
        return commentDao.getUserCommentCount(userId);
    }
}

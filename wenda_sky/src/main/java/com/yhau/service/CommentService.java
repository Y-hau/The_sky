package com.yhau.service;

import com.yhau.config.web.HostHandler;
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

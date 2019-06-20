package com.yhau.controller;

import com.yhau.config.web.HostHandler;
import com.yhau.core.async.EventModel;
import com.yhau.core.async.EventProducer;
import com.yhau.core.async.EventType;
import com.yhau.core.util.ResponseUtil;
import com.yhau.core.util.StaticUtil;
import com.yhau.model.Comment;
import com.yhau.service.CommentService;
import com.yhau.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController {
    @Autowired
    private LikeService likeService;
    @Autowired
    private HostHandler hostHandler;
    @Autowired
    private CommentService commentService;
    @Autowired
    private EventProducer eventProducer;

    @RequestMapping(value = "/like", method = {RequestMethod.POST})
    @ResponseBody

    public String like(@RequestParam("commentId") int commentId) {
        if (hostHandler.getUser() == null) {
            return ResponseUtil.getJSONString(999);
        }
        Comment comment = commentService.getCommentById(commentId);
        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                .setActorId(hostHandler.getUser().getId()).setEntityId(commentId)
                .setEntityType(StaticUtil.ENTITY_COMMENT).setEntityOwnerId(comment.getUserId())
                .setExt("questionId", String.valueOf(comment.getEntityId())));
        long likeCount = likeService.like(hostHandler.getUser().getId(), StaticUtil.ENTITY_COMMENT, commentId);
        return ResponseUtil.getJSONString(0, String.valueOf(likeCount));
    }

    @RequestMapping(value = "/dislike", method = {RequestMethod.POST})
    @ResponseBody
    public String dislike(@RequestParam("commentId") int commentId) {
        if (hostHandler.getUser() == null) {
            return ResponseUtil.getJSONString(999);
        }
        long disLikeCount = likeService.disLike(hostHandler.getUser().getId(), StaticUtil.ENTITY_COMMENT, commentId);
        return ResponseUtil.getJSONString(0, String.valueOf(disLikeCount));
    }
}

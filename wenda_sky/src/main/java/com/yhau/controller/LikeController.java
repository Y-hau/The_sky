package com.yhau.controller;

import com.yhau.config.web.HostHandler;
import com.yhau.core.util.ResponseUtil;
import com.yhau.model.Comment;
import com.yhau.service.CommentService;
import com.yhau.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LikeController {
    @Autowired
    private LikeService likeService;
    @Autowired
    private HostHandler hostHandler;
    @Autowired
    private CommentService commentService;

    public String like(@RequestParam("commentId") int commentId) {
        if (hostHandler.getUser() == null) {
            return ResponseUtil.getJSONString(999);
        }
        Comment comment = commentService.getCommentById(commentId);
        long likeCount = likeService.like(comment.getUserId(), comment.getEntityType(), comment.getEntityId());
        return ResponseUtil.getJSONString(0, String.valueOf(likeCount));
    }
}

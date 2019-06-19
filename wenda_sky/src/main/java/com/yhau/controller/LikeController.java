package com.yhau.controller;

import com.yhau.config.web.HostHandler;
import com.yhau.core.util.EntityType;
import com.yhau.core.util.ResponseUtil;
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

    @RequestMapping(value = "/like", method = {RequestMethod.POST})
    @ResponseBody
    public String like(@RequestParam("commentId") int commentId) {
        if (hostHandler.getUser() == null) {
            return ResponseUtil.getJSONString(999);
        }
        long likeCount = likeService.like(hostHandler.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);
        return ResponseUtil.getJSONString(0, String.valueOf(likeCount));
    }

    @RequestMapping(value = "/dislike", method = {RequestMethod.POST})
    @ResponseBody
    public String dislike(@RequestParam("commentId") int commentId) {
        if (hostHandler.getUser() == null) {
            return ResponseUtil.getJSONString(999);
        }
        long disLikeCount = likeService.disLike(hostHandler.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);
        return ResponseUtil.getJSONString(0, String.valueOf(disLikeCount));
    }
}

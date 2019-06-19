package com.yhau.controller;

import com.yhau.config.web.HostHandler;
import com.yhau.core.util.EntityType;
import com.yhau.core.util.ResponseUtil;
import com.yhau.model.Comment;
import com.yhau.model.Question;
import com.yhau.model.ViewObject;
import com.yhau.service.CommentService;
import com.yhau.service.LikeService;
import com.yhau.service.QuestionService;
import com.yhau.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class QuestionController {
    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @Resource
    private QuestionService questionService;

    @Resource
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private HostHandler hosthandler;

    @Autowired
    private LikeService likeService;

    @RequestMapping(value = "/question/add")
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title, @RequestParam("content") String content) {
        try {
            return questionService.addQuestion(title, content);
        } catch (Exception e) {
            logger.error("添加问题失败", e);
            return ResponseUtil.getJSONString(1, "添加问题失败");
        }
    }

    @RequestMapping(value = "/question/{qid}")
    public String questionDetail(HttpServletRequest request, Model model, @PathVariable("qid") int qid) {
        Question question = questionService.selectById(qid);
        model.addAttribute("question", question);
        List<Comment> commentList = commentService.getCommentsByEntity(qid, EntityType.ENTITY_QUESTION);
        List<ViewObject> vos = new ArrayList<>();
        for (Comment comment : commentList) {
            ViewObject vo = new ViewObject();
            vo.set("comment", comment);
            if (hosthandler.getUser() == null) {
                vo.set("liked", 0);
            } else {
                vo.set("liked", likeService.getLikeStatus(hosthandler.getUser().getId(), EntityType.ENTITY_COMMENT, comment.getId()));
            }
            vo.set("likeCount", likeService.getLikeCount(EntityType.ENTITY_COMMENT, comment.getId()));
            vo.set("user", userService.getUser(comment.getUserId()));
            vos.add(vo);
        }
        if (hosthandler.getUser() == null) {
            model.addAttribute("next", request.getRequestURI());
        }
        model.addAttribute("comments", vos);
        return "/detail.html";
    }
}

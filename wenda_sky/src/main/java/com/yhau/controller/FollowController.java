package com.yhau.controller;

import com.yhau.aspect.CheckLogin;
import com.yhau.config.web.HostHandler;
import com.yhau.core.async.EventModel;
import com.yhau.core.async.EventProducer;
import com.yhau.core.async.EventType;
import com.yhau.core.util.ResponseUtil;
import com.yhau.core.util.StaticUtil;
import com.yhau.model.Question;
import com.yhau.model.User;
import com.yhau.model.ViewObject;
import com.yhau.service.CommentService;
import com.yhau.service.FollowService;
import com.yhau.service.QuestionService;
import com.yhau.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class FollowController {
    @Autowired
    private HostHandler hostHandler;

    @Autowired
    private FollowService followService;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @RequestMapping(value = "/followUser", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String followUser(@RequestParam("userId") int userId) {
        if (hostHandler.getUser() == null) {
            return ResponseUtil.getJSONString(999);
        }
        boolean ret = followService.follow(hostHandler.getUser().getId(), StaticUtil.ENTITY_USER, userId);
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW).setActorId(hostHandler.getUser().getId())
                .setEntityId(userId).setEntityType(StaticUtil.ENTITY_USER).setEntityOwnerId(userId));
        //返回关注的人数
        return ResponseUtil.getJSONString(ret ? 0 : 1, String.valueOf(followService.getFolloweeCount(hostHandler.getUser().getId(), StaticUtil.ENTITY_USER)));
    }

    @RequestMapping(value = "/unfollowUser", method = {RequestMethod.POST})
    @ResponseBody
    @CheckLogin
    public String unFollowUser(@RequestParam("userId") int userId) {

        boolean ret = followService.unFollow(hostHandler.getUser().getId(), StaticUtil.ENTITY_USER, userId);
        //返回关注的人数
        return ResponseUtil.getJSONString(ret ? 0 : 1, String.valueOf(followService.getFolloweeCount(hostHandler.getUser().getId(), StaticUtil.ENTITY_USER)));
    }

    @RequestMapping(value = "/followQuestion", method = {RequestMethod.POST})
    @ResponseBody
    @CheckLogin
    public String followQuestion(@RequestParam("questionId") int questionId) {
        Question question = questionService.selectById(questionId);
        if (question == null) {
            return ResponseUtil.getJSONString(1, "问题不存在");
        }

        boolean ret = followService.follow(hostHandler.getUser().getId(), StaticUtil.ENTITY_QUESTION, questionId);

        eventProducer.fireEvent(new EventModel(EventType.FOLLOW).setActorId(hostHandler.getUser().getId())
                .setEntityId(questionId).setEntityType(StaticUtil.ENTITY_QUESTION).setEntityOwnerId(question.getUserId()));

        Map<String, Object> info = new HashMap<>();
        info.put("headUrl", hostHandler.getUser().getHeadUrl());
        info.put("name", hostHandler.getUser().getName());
        info.put("id", hostHandler.getUser().getId());
        info.put("count", followService.getFollowerCount(StaticUtil.ENTITY_QUESTION, questionId));

        return ResponseUtil.getJSONString(ret ? 0 : 1, info);
    }

    @RequestMapping(path = {"/unfollowQuestion"}, method = {RequestMethod.POST})
    @ResponseBody
    public String unfollowQuestion(@RequestParam("questionId") int questionId) {
        Question question = questionService.selectById(questionId);
        if (question == null) {
            return ResponseUtil.getJSONString(1, "问题不存在");
        }

        boolean ret = followService.unFollow(hostHandler.getUser().getId(), StaticUtil.ENTITY_QUESTION, questionId);

        Map<String, Object> info = new HashMap<>();
        info.put("id", hostHandler.getUser().getId());
        info.put("count", followService.getFollowerCount(StaticUtil.ENTITY_QUESTION, questionId));
        return ResponseUtil.getJSONString(ret ? 0 : 1, info);
    }

    @RequestMapping(path = {"/user/{uid}/followers"}, method = {RequestMethod.GET})
    public String followers(Model model, @PathVariable("uid") int userId) {
        List<Integer> followerIds = followService.getFollowers(StaticUtil.ENTITY_USER, userId, 0, 10);
        if (hostHandler.getUser() != null) {
            model.addAttribute("followers", getUsersInfo(hostHandler.getUser().getId(), followerIds));
        } else {
            model.addAttribute("followers", getUsersInfo(0, followerIds));
        }
        model.addAttribute("followerCount", followService.getFollowerCount(StaticUtil.ENTITY_USER, userId));
        model.addAttribute("curUser", userService.getUser(userId));
        return "/followers.html";
    }

    @RequestMapping(path = {"/user/{uid}/followees"}, method = {RequestMethod.GET})
    public String followees(Model model, @PathVariable("uid") int userId) {
        List<Integer> followeeIds = followService.getFollowees(userId, StaticUtil.ENTITY_USER, 0, 10);

        if (hostHandler.getUser() != null) {
            model.addAttribute("followees", getUsersInfo(hostHandler.getUser().getId(), followeeIds));
        } else {
            model.addAttribute("followees", getUsersInfo(0, followeeIds));
        }
        model.addAttribute("followeeCount", followService.getFolloweeCount(userId, StaticUtil.ENTITY_USER));
        model.addAttribute("curUser", userService.getUser(userId));
        return "/followees.html";
    }

    private List<ViewObject> getUsersInfo(int localUserId, List<Integer> userIds) {
        List<ViewObject> userInfos = new ArrayList<ViewObject>();
        for (Integer uid : userIds) {
            User user = userService.getUser(uid);
            if (user == null) {
                continue;
            }
            ViewObject vo = new ViewObject();
            vo.set("user", user);
            vo.set("commentCount", commentService.getUserCommentCount(uid));
            vo.set("followerCount", followService.getFollowerCount(StaticUtil.ENTITY_USER, uid));
            vo.set("followeeCount", followService.getFolloweeCount(uid, StaticUtil.ENTITY_USER));
            if (localUserId != 0) {
                vo.set("followed", followService.isFollower(localUserId, StaticUtil.ENTITY_USER, uid));
            } else {
                vo.set("followed", false);
            }
            userInfos.add(vo);
        }
        return userInfos;
    }
}

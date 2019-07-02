package com.yhau.core.async.handler;

import com.alibaba.fastjson.JSONObject;
import com.yhau.core.async.EventHandler;
import com.yhau.core.async.EventModel;
import com.yhau.core.async.EventType;
import com.yhau.core.util.RedisClient;
import com.yhau.core.util.RedisKeyUtil;
import com.yhau.core.util.StaticUtil;
import com.yhau.dao.FeedDao;
import com.yhau.dao.QuestionDao;
import com.yhau.model.Feed;
import com.yhau.model.Question;
import com.yhau.model.User;
import com.yhau.service.FollowService;
import com.yhau.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

@Component
public class FeedHandler implements EventHandler {
    @Autowired
    FollowService followService;

    @Autowired
    UserService userService;

    @Resource
    FeedDao feedDao;

    @Autowired
    RedisClient redisClient;

    @Resource
    QuestionDao questionDao;

    private String buildFeedData(EventModel model) {
        Map<String, String> map = new HashMap<>();
        //触发用户是通用的
        User actor = userService.getUser(model.getActorId());
        if (actor == null) {
            return null;
        }
        map.put("userId", String.valueOf(actor.getId()));
        map.put("userHead", actor.getHeadUrl());
        map.put("userName", actor.getName());
        if (model.getType() == EventType.COMMENT ||
                (model.getType() == EventType.FOLLOW && model.getEntityType() == StaticUtil.ENTITY_QUESTION)) {
            Question question = questionDao.selectById(model.getEntityId());
            if (question == null) {
                return null;
            }
            map.put("questionId", String.valueOf(question.getId()));
            map.put("questionTitle", question.getTitle());
            return JSONObject.toJSONString(map);
        }
        return null;
    }

    @Override
    public void doHandler(EventModel model) {
        Feed feed = new Feed();
        feed.setCreatedDate(new Date());
        feed.setType(model.getType().getValue());
        feed.setUserId(model.getActorId());
        feed.setData(buildFeedData(model));
        if (feed.getData() == null) {
            // 不支持的feed
            return;
        }
        feedDao.addFeed(feed);

        // 获得所有粉丝
        List<Integer> followers = followService.getFollowers(StaticUtil.ENTITY_USER, model.getActorId(), Integer.MAX_VALUE);
        // 系统队列
        followers.add(0);
        // 给所有粉丝推事件
        for (int follower : followers) {
            String timelineKey = RedisKeyUtil.getTimelineKey(follower);
            redisClient.lpush(timelineKey, String.valueOf(feed.getId()));
            // 限制最长长度，如果timelineKey的长度过大，就删除后面的新鲜事
        }
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(new EventType[]{EventType.COMMENT, EventType.FOLLOW});
    }
}

package com.yhau.service;

import com.yhau.config.web.HostHandler;
import com.yhau.core.util.RedisClient;
import com.yhau.core.util.RedisKeyUtil;
import com.yhau.core.util.StaticUtil;
import com.yhau.dao.FeedDao;
import com.yhau.model.Feed;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class FeedService {

    @Resource
    private FeedDao feedDao;

    @Resource
    private HostHandler hostHandler;

    @Resource
    private RedisClient redisClient;

    @Resource
    private FollowService followService;

    public List<Feed> getPushFeeds(int start, int end) {
        int localUserId = hostHandler.getUser() != null ? hostHandler.getUser().getId() : 0;
        List<String> feedIds = redisClient.lrange(RedisKeyUtil.getTimelineKey(localUserId), start, end);
        List<Feed> feeds = null;
        for (String feedId : feedIds) {
            Feed feed = feedDao.getFeedById(Integer.parseInt(feedId));
            if (feed != null) {
                feeds.add(feed);
            }
        }
        return feeds;
    }

    public List<Feed> getPullFeeds() {
        int localUserId = hostHandler.getUser() != null ? hostHandler.getUser().getId() : 0;
        List<Integer> followees = null;
        if (localUserId != 0) {
            //关注的人
            followees = followService.getFollowees(localUserId, StaticUtil.ENTITY_USER, Integer.MAX_VALUE);
        }
        List<Feed> feeds = feedDao.selectUserFeeds(Integer.MAX_VALUE, followees, 10);
        return feeds;
    }
}

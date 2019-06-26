package com.yhau.service;

import com.yhau.core.util.RedisClient;
import com.yhau.core.util.RedisKeyUtil;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class FollowService {

    @Resource
    private RedisClient redisClient;

    /**
     * 用户关注了某个实体,可以关注问题,关注用户,关注评论等任何实体
     *
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public boolean follow(int userId, int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        Date date = new Date();
        Jedis jedis = redisClient.getJedis();
        Transaction multi = redisClient.multi(jedis);
        //针对于该类实体，粉丝增加（当前用户）
        redisClient.zadd(followerKey, date.getTime(), String.valueOf(userId));
        //针对于当前用户，增加对该类实体的关注
        redisClient.zadd(followeeKey, date.getTime(), String.valueOf(entityId));

        List<Object> ret = redisClient.exec(multi, jedis);
        return (ret.size() == 2) && ((long) ret.get(0) > 0) && ((long) ret.get(1) > 0);
    }

    /**
     * 取消关注
     *
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public boolean unFollow(int userId, int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);

        Jedis jedis = redisClient.getJedis();
        Transaction multi = redisClient.multi(jedis);

        redisClient.zrem(followerKey, String.valueOf(userId));
        redisClient.zrem(followeeKey, String.valueOf(entityType));

        List<Object> exec = redisClient.exec(multi, jedis);
        return (exec.size() == 2) && ((long) exec.get(0) > 0) && ((long) exec.get(1) > 0);
    }

    public List<Integer> getFollowers(int entityType, int entityId, int count) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return getIdsFromSet(redisClient.zrevrange(followerKey, 0, count));
    }

    public List<Integer> getFollowers(int entityType, int entityId, int offset, int count) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return getIdsFromSet(redisClient.zrevrange(followerKey, offset, offset + count));
    }

    public List<Integer> getFollowees(int userId, int entityType, int count) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return getIdsFromSet(redisClient.zrevrange(followeeKey, 0, count));
    }

    public List<Integer> getFollowees(int userId, int entityType, int offset, int count) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return getIdsFromSet(redisClient.zrevrange(followeeKey, offset, offset + count));
    }

    public long getFollowerCount(int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return redisClient.zcard(followerKey);
    }

    public long getFolloweeCount(int userId, int entityType) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return redisClient.zcard(followeeKey);
    }

    private List<Integer> getIdsFromSet(Set<String> idset) {
        List<Integer> ids = new ArrayList<>();
        for (String str : idset) {
            ids.add(Integer.parseInt(str));
        }
        return ids;
    }

    /**
     * 判断该用户是否关注了某实体
     *
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public boolean isFollower(int userId, int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return redisClient.zscore(followerKey, String.valueOf(userId)) != null;
    }
}

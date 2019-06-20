package com.yhau.core.async;

import com.alibaba.fastjson.JSONObject;
import com.yhau.core.util.RedisClient;
import com.yhau.core.util.RedisKeyUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 事件生产者
 */
@Service
public class EventProducer {
    @Resource
    private RedisClient redisClient;

    public boolean fireEvent(EventModel eventModel) {
        try {
            String json = JSONObject.toJSONString(eventModel);
            String key = RedisKeyUtil.getEventQueueKey();
            redisClient.lpush(key, json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

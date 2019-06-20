package com.yhau.core.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

@Component
public class RedisClient {

    private static final Logger logger = LoggerFactory.getLogger(RedisClient.class);

    @Autowired
    private JedisPool jedisPool;

    public Jedis getJedis() {
        return jedisPool.getResource();
    }

    /**
     * Set集合 添加
     *
     * @param key
     * @param value
     */
    public long sadd(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.sadd(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("发生异常" + e.getMessage());
        } finally {
            close(jedis);
        }
        return 0;
    }

    /**
     * 返回Set集合的数量
     *
     * @param key
     * @return
     */
    public long scard(String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.scard(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            close(jedis);
        }
        return 0;
    }

    /**
     * 删除Set集合中某值
     *
     * @param key
     * @param value
     * @return
     */
    public long srem(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.srem(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            close(jedis);
        }
        return 0;
    }

    /**
     * Set集合中是否存在某值
     *
     * @param key
     * @param value
     * @return
     */
    public boolean sismember(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.sismember(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            close(jedis);
        }
        return false;
    }

    /**
     * 向list中插入数据  （redis中的list有点类似于队列）
     *
     * @param key
     * @param value
     * @return
     */
    public long lpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            close(jedis);
        }
        return 0;
    }

    /**
     * 移出并获取列表的最后一个元素， 如果列表没有元素会阻塞
     * 列表直到等待超时或发现可弹出元素为止。
     *
     * @param timeout
     * @param key
     * @return
     */
    public List<String> brpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.brpop(timeout, key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            close(jedis);
        }
        return null;
    }

    public void close(final Jedis jedis) {
        if (null != jedis) {
            jedis.close();
        }
    }
}
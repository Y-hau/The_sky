package com.yhau.core.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.Set;

@Component
public class RedisClient {

    private static final Logger logger = LoggerFactory.getLogger(RedisClient.class);

    @Autowired
    private JedisPool jedisPool;

    public Jedis getJedis() {
        return jedisPool.getResource();
    }

    public String set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.set(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("发生异常" + e.getMessage());
        } finally {
            close(jedis);
        }
        return null;
    }

    public String get(String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.get(key);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("发生异常" + e.getMessage());
        } finally {
            close(jedis);
        }
        return null;
    }

    public long del(String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.del(key);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("发生异常" + e.getMessage());
        } finally {
            close(jedis);
        }
        return 0;
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

    /**
     * 标记一个事务块的开始
     *
     * @return
     */
    public Transaction multi() {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.multi();
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            close(jedis);
        }
        return null;
    }

    public long zadd(String key, double score, String value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.zadd(key, score, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            close(jedis);
        }
        return 0;
    }

    public long zrem(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.zrem(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            close(jedis);
        }
        return 0;
    }

    public long zcard(String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.zcard(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            close(jedis);
        }
        return 0;
    }

    public Set<String> zrevrange(String key, int start, int end) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.zrevrange(key, start, end);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            close(jedis);
        }
        return null;
    }

    public Double zscore(String key, String member) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.zscore(key, member);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            close(jedis);
        }
        return null;
    }

    /**
     * 执行所有事物块内的所有命令
     * (发生异常时，取消事务，放弃执行事务块内的所有事务)
     *
     * @param tx
     * @return
     */
    public List<Object> exec(Transaction tx) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return tx.exec();
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            tx.discard();
        } finally {
            close(jedis, tx);
        }
        return null;
    }

    public void close(final Jedis jedis) {

        this.close(jedis, null);
    }

    public void close(final Jedis jedis, final Transaction tx) {
        if (null != jedis) {
            jedis.close();
        }
        if (null != tx) {
            tx.close();
        }
    }
}
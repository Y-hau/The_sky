package com.yhau.config.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {
    private Logger logger = LoggerFactory.getLogger(RedisConfig.class);

    /*@Bean(name = "jedis.pool")
    @Autowired
    public JedisSentinelPool jedisPool(@Qualifier("jedis.pool.config") JedisPoolConfig config,
                                       @Value("${spring.redis.sentinel.master}") String clusterName,
                                       @Value("${spring.redis.sentinel.nodes}") String sentinelNodes,
                                       @Value("${spring.redis.timeout}") int timeout,
                                       @Value("${spring.redis.password}") String password) {
        logger.info("缓存服务器的主服务名称：" + clusterName + ", 主从服务ip&port:" + sentinelNodes);
        Assert.isTrue(StringUtils.isNotEmpty(clusterName), "主服务名称配置为空");
        Assert.isTrue(StringUtils.isNotEmpty(sentinelNodes), "主从服务地址配置为空");

        Set<String> sentinels = Sets.newHashSet(StringUtils.split(sentinelNodes, ","));

        JedisSentinelPool sentinelJedisPool = new JedisSentinelPool(clusterName, sentinels, config, Protocol.DEFAULT_TIMEOUT, password);

        return sentinelJedisPool;
    }*/

    @Bean
    public JedisPool jedisPool(@Qualifier("jedis.pool.config") JedisPoolConfig config,
                               @Value("${spring.redis.host}") String host,
                               @Value("${spring.redis.port}") Integer port,
                               @Value("${spring.redis.timeout}") int timeout,
                               @Value("${spring.redis.password}") String password,
                               @Value("${spring.redis.database}") Integer database) {
        JedisPool jedisPool = new JedisPool(config, host, port, timeout, password, database);
        return jedisPool;
    }

    @Bean(name = "jedis.pool.config")
    public JedisPoolConfig jedisPoolConfig(@Value("${spring.redis.jedis.pool.maxTotal}") int maxTotal,
                                           @Value("${spring.redis.jedis.pool.maxIdle}") int maxIdle,
                                           @Value("${spring.redis.jedis.pool.maxWaitMillis}") int maxWaitMillis,
                                           @Value("${spring.redis.jedis.pool.testOnBorrow}") boolean testOnBorrow,
                                           @Value("${spring.redis.jedis.pool.testOnReturn}") boolean testOnReturn,
                                           @Value("${spring.redis.jedis.pool.blockWhenExhausted}") boolean blockWhenExhausted,
                                           @Value("${spring.redis.jedis.pool.testWhileIdle}") boolean testWhileIdle,
                                           @Value("${spring.redis.jedis.pool.timeBetweenEvictionRunsMillis}") long timeBetweenEvictionRunsMillis,
                                           @Value("${spring.redis.jedis.pool.numTestsPerEvictionRun}") int numTestsPerEvictionRun,
                                           @Value("${spring.redis.jedis.pool.minEvictableIdleTimeMillis}") long minEvictableIdleTimeMillis) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMaxWaitMillis(maxWaitMillis);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        config.setBlockWhenExhausted(blockWhenExhausted);
        config.setTestWhileIdle(testWhileIdle);
        config.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        config.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
        config.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);

        return config;
    }
}
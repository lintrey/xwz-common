package com.wanzi.common.util;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

import java.util.Collections;
import java.util.UUID;

/**
 * @program: common
 * @description: 为了确保分布式锁可用，我们至少要确保锁的实现同时满足以下四个条件：
 * 互斥性。在任意时刻，只有一个客户端能持有锁。
 * 不会发生死锁。即使有一个客户端在持有锁的期间崩溃而没有主动解锁，也能保证后续其他客户端能加锁。
 * 具有容错性。只要大部分的Redis节点正常运行，客户端就可以加锁和解锁。
 * 解铃还须系铃人。加锁和解锁必须是同一个客户端，客户端自己不能把别人加的锁给解了。
 * @author: zhangchuntao
 * @create: 2018-08-10
 **/
@Slf4j
public class RedisLock {

    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";


    private static final Long RELEASE_SUCCESS = 1L;

    /**
     * 释放分布式锁
     * @param jedis Redis客户端
     * @param lockKey 锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public static boolean releaseDistributedLock(Jedis jedis, String lockKey, String requestId) {

        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));

        if (RELEASE_SUCCESS.equals(result)) {
            return true;
        }
        return false;

    }


    /**
     * 尝试获取分布式锁
     * @param jedis Redis客户端
     * @param lockKey 锁
     * @param requestId 请求标识
     * @param expireTime 超期时间
     * @return 是否获取成功
     */
    public static boolean tryGetDistributedLock(Jedis jedis, String lockKey, String requestId, int expireTime) {

        String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);

        if (LOCK_SUCCESS.equals(result)) {
            return true;
        }
        return false;

    }


    public void unlock(String locaName) {
        JedisPool jedisPool = new JedisPool("172.30.1.211",6379);

        Jedis conn = null;
        try {
            conn = jedisPool.getResource();
            conn.auth("iboxpay");

            conn.select(5);

            String lockKey = "lock:" + locaName;

            conn.del(new byte[][]{lockKey.getBytes()});
        } catch (Exception var7) {
            log.error("{}", var7.getMessage(), var7);
        } finally {
            conn.close();
        }

    }


    /**
     * 加锁
     * @param locaName  锁的key
     * @param acquireTimeout  获取超时时间
     * @param timeout   锁的超时时间
     * @return 锁标识
     */
    public boolean lockWithTimeout(String locaName,
                                  long acquireTimeout, long timeout) {

        JedisPool jedisPool = new JedisPool("172.30.1.211",6379);

        Jedis conn = null;
        String retIdentifier = null;

        boolean val3 = false;
        try {
            // 获取连接
            conn = jedisPool.getResource();
            conn.auth("iboxpay");

            conn.select(5);

            // 随机生成一个value
            String identifier = UUID.randomUUID().toString();
            // 锁名，即key值
            String lockKey = "lock:" + locaName;
            // 超时时间，上锁后超过此时间则自动释放锁
            int lockExpire = (int)(timeout / 1000);

            // 获取锁的超时时间，超过这个时间则放弃获取锁
            long end = System.currentTimeMillis() + acquireTimeout;
            while (System.currentTimeMillis() < end) {
                if (conn.setnx(lockKey, identifier) == 1) {

                    conn.expire(lockKey, lockExpire);
                    // 返回value值，用于释放锁时间确认
                    retIdentifier = identifier;
                    val3 = true;
                    return val3;
                }
                // 返回-1代表key没有设置超时时间，为key设置一个超时时间
                if (conn.ttl(lockKey) == -1) {
                    conn.expire(lockKey, lockExpire);
                }

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        } catch (JedisException e) {
            unlock(locaName);
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return val3;
    }


}

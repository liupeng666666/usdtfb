package com.whp.usdtfb.Util.utils;

import com.alibaba.fastjson.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class RedisUtils {
    private static JedisPool pool = null;

    static {
        if (pool == null) {
            JedisPoolConfig config = new JedisPoolConfig();
            //控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
            //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
            config.setMaxTotal(500);
            //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
            config.setMaxIdle(5);
            //表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
            config.setMaxWaitMillis(1000 * 100);
            //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
            config.setTestOnBorrow(true);
            if (PropertiesUtils.KeyValue("redis.state").equals("true")) {
                pool = new JedisPool(config, PropertiesUtils.KeyValue("redis.host"), Integer.parseInt(PropertiesUtils.KeyValue("redis.port")), 10000, PropertiesUtils.KeyValue("redis.auth"));
            } else {
                pool = new JedisPool(config, PropertiesUtils.KeyValue("redis.host"), Integer.parseInt(PropertiesUtils.KeyValue("redis.port")));
            }

        }
    }

    public static void returnResource(Jedis redis) {
        if (redis != null) {
            redis.close();
        }
    }

    /**
     * 方法描述 获取Jedis实例
     *
     * @return
     * @author yaomy
     * @date 2018年1月11日 下午4:56:58
     */
    public static Jedis getJedis() {
        Jedis jedis = pool.getResource();
        return jedis;
    }


    public static String get(String key, int select) {
        String time = "";
        try {
            Jedis jedis = getJedis();
            jedis.select(select);
            time = jedis.get(key);
            returnResource(jedis);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    public static void set(String key, String value, int select) {
        Jedis jedis = getJedis();
        jedis.select(select);
        jedis.set(key, value);
        returnResource(jedis);
    }

    public static void time_set(String key, String value, int select, long time) {
        Jedis jedis = getJedis();
        jedis.select(select);
        jedis.set(key, value, "NX", "EX", time);
        returnResource(jedis);
    }

    public static void hset(String key, String value, int select, String pid) {
        Jedis jedis = getJedis();
        jedis.select(select);
        jedis.hset(key, pid, value);
        returnResource(jedis);
    }

    public static String hget(String key, int select, String pid) {
        String value = "";
        try {
            Jedis jedis = getJedis();
            jedis.select(select);
            value = jedis.hget(key, pid);
            returnResource(jedis);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public static Map<String, String> hgetall(String key, int select) {
        Jedis jedis = getJedis();
        jedis.select(select);
        Map<String, String> map = jedis.hgetAll(key);
        returnResource(jedis);
        return map;
    }

    public static void hdel(String key, String pid, int select) {
        Jedis jedis = getJedis();
        jedis.select(select);
        jedis.hdel(key, pid);
        returnResource(jedis);
    }

    public static void del(String key, int select) {
        Jedis jedis = getJedis();
        jedis.select(select);
        jedis.del(key);
        returnResource(jedis);
    }
}

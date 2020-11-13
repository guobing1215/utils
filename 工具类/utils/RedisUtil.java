package com.springboot.core.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {
    /**
     * 是否开启redis缓存  true开启   false关闭
     */
    private boolean open = true;

    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired(required = false)
    private ValueOperations<String, String> valueOperations;

    @Autowired(required = false)
    private HashOperations<String, String, Object> hashOperations;

    @Autowired(required = false)
    private ListOperations<String, Object> listOperations;

    @Autowired(required = false)
    private SetOperations<String, Object> setOperations;

    @Autowired(required = false)
    private ZSetOperations<String, Object> zSetOperations;

    /**
     * 默认过期时长，单位：秒
     */
    public final static long DEFAULT_EXPIRE = 7200;

    /**
     * 不设置过期时长
     */
    public final static long NOT_EXPIRE = -1;
    public final static Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().disableHtmlEscaping()
            .create();

    public boolean exists(String key) {
        if (!open) {
            return false;
        }

        return redisTemplate.hasKey(key);
    }

    /**
     *
     * @param key
     * @param increment
     * @return
     */
    public Double incrByFloat(String key, double increment) {
        return redisTemplate.opsForValue().increment(key, increment);
    }
    /**
     * 设置过期时间
     *
     * @param key
     * @param timeout
     * @param unit
     * @return
     */
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    public void set(String key, Object value, long expire) {
        if (!open) {
            return;
        }
        try {
            valueOperations.set(key, toJson(value));

        } catch (Exception e0) {
            System.out.println(e0.getMessage());
        }
        if (expire != NOT_EXPIRE) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    public void set(String key, Object value) {
        if (!open) {
            return;
        }
        try {

            valueOperations.set(key, toJson(value));

        } catch (Exception e0) {
            System.out.println(e0.getMessage());
        }
    }

    public <T> T get(String key, Class<T> clazz, long expire) {
        if (!open) {
            return null;
        }

        String value = valueOperations.get(key);
        if (expire != NOT_EXPIRE) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value == null ? null : fromJson(value, clazz);
    }

    public <T> T get(String key, Class<T> clazz) {
        if (!open) {
            return null;
        }

        return get(key, clazz, NOT_EXPIRE);
    }

    public String get(String key, long expire) {
        if (!open) {
            return null;
        }

        String value = valueOperations.get(key);
        if (expire != NOT_EXPIRE) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value;
    }

    public String get(String key) {
        if (!open) {
            return null;
        }

        return get(key, NOT_EXPIRE);
    }

    /**
     * 获取key对应的过期时间
     *
     * @param key redis中key
     * @return java.lang.Long
     * @author anYanSen
     * @since 2019/9/26 15:42
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
    }

    public void delete(String key) {
        if (!open) {
            return;
        }

        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    public void delete(String... keys) {
        if (!open) {
            return;
        }

        for (String key : keys) {
            redisTemplate.delete(key);
        }
    }

    public void deletePattern(String pattern) {
        if (!open) {
            return;
        }

        Set<String> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0) {
            redisTemplate.delete(keys);
        }
    }

    /**
     * 是否有key
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * Object转成JSON数据
     */
    private String toJson(Object object) {
        if (!open) {
            return null;
        }

        if (object instanceof Integer || object instanceof Long || object instanceof Float ||
                object instanceof Double || object instanceof Boolean || object instanceof String) {
            return String.valueOf(object);
        }

        return gson.toJson(object);
    }

    /**
     * JSON数据，转成Object
     */
    private <T> T fromJson(String json, Class<T> clazz) {
        if (!open) {
            return null;
        }

        return gson.fromJson(json, clazz);
    }

}
package com.whp.usdtfb.user.utils;

import com.whp.usdtfb.Util.utils.RedisUtils;
import com.whp.usdtfb.user.Interface.FbOrderInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2018/12/29 15:36
 * @descrpition :
 */
@Component
public class TaskUtils {
    @Autowired
    private FbOrderInterface fbOrderInterface;


    @Scheduled(initialDelay = 1000, fixedRate = 6000)
    public void task() {
        Map<String, String> value = RedisUtils.hgetall("fb.order", 5);
        long datetime = new Date().getTime();
        for (String key : value.keySet()) {
            System.out.println(key + "--" + value.get(key));
            long time = Long.parseLong(value.get(key));
            if (time <= datetime) {
                RedisUtils.hdel("fb.order", key, 5);
                Map<String, Object> map = new HashMap<>();
                map.put("pid", key);
                fbOrderInterface.FbOrderSf(map);
            }
        }

    }
}

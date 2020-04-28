package com.whp.usdtfb.user.Impl;

import com.alibaba.fastjson.JSONObject;
import com.whp.usdtfb.user.Dao.FbUserDao;
import com.whp.usdtfb.user.Interface.FbUserInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2018/12/18 14:34
 * @descrpition :
 */
@Service
public class FbUserImpl implements FbUserInterface {
    @Autowired
    private FbUserDao fbUserDao;

    @Override
    public JSONObject FbUserSelect(String pid) {
        JSONObject json = new JSONObject();
        try {
            Map<String, Object> map = fbUserDao.FbUserSelect(pid);
            json.put("user", map);
            json.put("code", 100);
        } catch (Exception e) {
            e.printStackTrace();
            json.put("code", 103);
        }
        return json;
    }

    @Override
    public JSONObject FbUserUpdate(Map<String, Object> map) {
        JSONObject json = new JSONObject();
        try {
            fbUserDao.FbUserUpdate(map);
            json.put("code", 100);
        } catch (Exception e) {
            e.printStackTrace();
            json.put("code", 103);
        }
        return json;
    }

    @Override
    public JSONObject FbUserUpdateQ(Map<String, Object> map) {
        JSONObject json = new JSONObject();
        try {
            if (map.containsKey("reputation")) {
                int num = fbUserDao.FbUserReputation(map);
                int p = Integer.parseInt(map.get("reputation").toString());
                if (p + num > 100) {
                    p = 100;
                } else if (p + num < 0) {
                    p = 0;
                } else {
                    p = p + num;
                }
                map.put("reputation", p);
            }
            fbUserDao.FbUserUpdateQ(map);
            json.put("code", 100);
        } catch (Exception e) {
            e.printStackTrace();
            json.put("code", 103);
        }
        return json;
    }
}

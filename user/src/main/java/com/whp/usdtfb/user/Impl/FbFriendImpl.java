package com.whp.usdtfb.user.Impl;

import com.alibaba.fastjson.JSONObject;
import com.whp.usdtfb.user.Dao.FbFriendDao;
import com.whp.usdtfb.user.Interface.FbFriendInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2019/1/3 13:45
 * @descrpition :
 */
@Service
public class FbFriendImpl implements FbFriendInterface {

    @Autowired
    private FbFriendDao fbFriendDao;

    @Override
    public JSONObject FbFriendInsert(Map<String, Object> map) {
        JSONObject json = new JSONObject();
        try {
            int count = fbFriendDao.FbFriendCount(map);
            if (count == 0) {
                fbFriendDao.FbFriendInsert(map);
                json.put("code", 100);
            } else {
                json.put("code", 102);
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.put("code", 103);
        }
        return json;
    }
}

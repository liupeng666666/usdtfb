package com.whp.usdtfb.user.Impl;

import com.alibaba.fastjson.JSONObject;
import com.whp.usdtfb.Util.utils.MD5Util;
import com.whp.usdtfb.user.Dao.FbPlayDao;
import com.whp.usdtfb.user.Dao.SubUserDao;
import com.whp.usdtfb.user.Interface.FbPlayInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2018/12/18 17:13
 * @descrpition :
 */
@Service
public class FbPlayImpl implements FbPlayInterface {
    @Autowired
    private FbPlayDao fbPlayDao;
    @Autowired
    private SubUserDao subUserDao;

    @Override
    public JSONObject FbBankSelect() {
        JSONObject json = new JSONObject();
        try {
            List<Map<String, Object>> list = fbPlayDao.FbBankSelect();
            json.put("code", 100);
            json.put("bank", list);
        } catch (Exception e) {
            e.printStackTrace();
            json.put("code", 103);
        }
        return json;
    }

    @Override
    public JSONObject FbPlaySelect(Map<String, Object> map) {
        JSONObject json = new JSONObject();
        try {
            List<Map<String, Object>> list = fbPlayDao.FbPlaySelect(map);
            json.put("code", 100);
            json.put("play", list);
        } catch (Exception e) {
            e.printStackTrace();
            json.put("code", 103);
        }
        return json;
    }

    @Override
    public JSONObject FbPlayInsert(Map<String, Object> map) {
        JSONObject json = new JSONObject();
        try {
            String password = MD5Util.MD5(map.get("password").toString());
            map.put("password", password);
            int num = subUserDao.SubUserCount(map);
            if (num > 0) {
                int sl = fbPlayDao.FbPlayCount(map);
                if (sl == 0) {
                    fbPlayDao.FbPlayInsert(map);
                }
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

    @Override
    public JSONObject FbPlayUpdate(Map<String, Object> map) {
        JSONObject json = new JSONObject();
        try {
            String password = MD5Util.MD5(map.get("password").toString());
            map.put("password", password);
            int num = subUserDao.SubUserCount(map);
            if (num > 0) {
                fbPlayDao.FbPlayUpdate(map);
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

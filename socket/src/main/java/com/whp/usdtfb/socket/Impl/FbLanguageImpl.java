package com.whp.usdtfb.socket.Impl;

import com.alibaba.fastjson.JSONObject;
import com.whp.usdtfb.socket.Dao.FbLanguageDao;
import com.whp.usdtfb.socket.Interface.FbLanguageInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2019/1/3 4:36
 * @descrpition :
 */
@Service
public class FbLanguageImpl implements FbLanguageInterface {

    @Autowired
    private FbLanguageDao fbLanguageDao;

    @Override
    public JSONObject FbLanguageInsert(Map<String, Object> map) {
        JSONObject json = new JSONObject();
        try {
            fbLanguageDao.FbLanguageInsert(map);
            json.put("code", 100);
        } catch (Exception e) {
            e.printStackTrace();
            json.put("code", 103);
        }
        return json;
    }

    @Override
    public JSONObject FbLanguageSelect(String userid) {

        JSONObject json = new JSONObject();
        try {
            List<Map<String, Object>> list = fbLanguageDao.FbLanguageSelect(userid);
            json.put("language", list);
            json.put("code", 100);
        } catch (Exception e) {
            e.printStackTrace();
            json.put("code", 103);
        }
        return json;
    }
}

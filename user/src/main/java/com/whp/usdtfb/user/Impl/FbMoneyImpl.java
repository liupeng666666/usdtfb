package com.whp.usdtfb.user.Impl;

import com.alibaba.fastjson.JSONObject;
import com.whp.usdtfb.user.Dao.FbMoneyDao;
import com.whp.usdtfb.user.Interface.FbMoneyInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2018/12/24 14:21
 * @descrpition :
 */
@Service
public class FbMoneyImpl implements FbMoneyInterface {
    @Autowired
    private FbMoneyDao fbMoneyDao;

    @Override
    public JSONObject FbMoneySelect(String currencyid, String userid, String lock) {
        JSONObject json = new JSONObject();
        try {
            Map<String, Object> map = fbMoneyDao.FbMoneySelect(currencyid, userid, lock);
            if (map == null) {
                fbMoneyDao.FbMoneyInsert(currencyid, userid);
                map = fbMoneyDao.FbMoneySelect(currencyid, userid, lock);
            }
            json.put("code", 100);
            json.put("money", map);
        } catch (Exception e) {
            e.printStackTrace();
            json.put("code", 103);
        }
        return json;
    }

    @Override
    public JSONObject FbMoneyUpdate(Map<String, Object> map) {
        JSONObject json = new JSONObject();
        try {
            fbMoneyDao.FbMoneyUpdate(map);
            json.put("code", 100);
        } catch (Exception e) {
            e.printStackTrace();
            json.put("code", 103);
        }

        return json;
    }
}

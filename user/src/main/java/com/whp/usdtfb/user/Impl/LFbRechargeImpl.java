package com.whp.usdtfb.user.Impl;

import com.alibaba.fastjson.JSONObject;
import com.whp.usdtfb.user.Dao.LFbRechargeDao;
import com.whp.usdtfb.user.Interface.LFbRechargeInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author : 张吉玲
 * @data : 2019/01/02 22:28
 * @descrpition :
 */
@Service
public class LFbRechargeImpl implements LFbRechargeInterface {

    @Autowired
    private LFbRechargeDao lFbRechargeDao;

    @Override
    public JSONObject LFbRechargeSelect(String userid, Map<String, Object> map) {
        JSONObject json = new JSONObject();
        try {
            List<Map<String, Object>> list = lFbRechargeDao.LFbRechargeSelect(userid, map,
                    Integer.parseInt(map.get("page").toString()), Integer.parseInt(map.get("num").toString()));

            int count = lFbRechargeDao.LFbRechargeCnt(userid, map);

            json.put("rechargeList", list);
            json.put("count", count);
            json.put("code", 100);
        } catch (Exception e) {
            e.printStackTrace();
            json.put("code", 103);
        }

        return json;
    }
}

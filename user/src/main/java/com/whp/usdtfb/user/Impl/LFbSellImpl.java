package com.whp.usdtfb.user.Impl;

import com.alibaba.fastjson.JSONObject;
import com.whp.usdtfb.user.Dao.LFbSellDao;
import com.whp.usdtfb.user.Interface.LFbSellInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author : 张吉玲
 * @data : 2018/12/31 22:28
 * @descrpition :
 */
@Service
public class LFbSellImpl implements LFbSellInterface {

    @Autowired
    private LFbSellDao lfbSellDao;

    @Override
    public JSONObject LFbSellSelect(Map<String, Object> map) {
        JSONObject json = new JSONObject();
        try {
            System.out.println(map.get("pay"));
            List<Map<String, Object>> list = lfbSellDao.LFbSellSelect(map, Integer.parseInt(map.get("page").toString()), Integer.parseInt(map.get("num").toString()));
            int count = lfbSellDao.LFbSellCount(map);
            json.put("sell", list);
            json.put("total", count);
            json.put("code", 100);
        } catch (Exception e) {
            e.printStackTrace();
            json.put("code", 103);
        }

        return json;
    }
}

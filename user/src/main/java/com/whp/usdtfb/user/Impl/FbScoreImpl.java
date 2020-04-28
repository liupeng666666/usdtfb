package com.whp.usdtfb.user.Impl;

import com.alibaba.fastjson.JSONObject;
import com.whp.usdtfb.user.Dao.FbScoreDao;
import com.whp.usdtfb.user.Interface.FbScoreInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2019/1/2 15:40
 * @descrpition :
 */
@Service
public class FbScoreImpl implements FbScoreInterface {
    @Autowired
    private FbScoreDao fbScoreDao;

    @Override
    public JSONObject FbScoreSelect() {
        JSONObject json = new JSONObject();
        try {
            List<Map<String, Object>> list = fbScoreDao.FbScoreSelectQ();
            json.put("code", 100);
            json.put("score", list);
        } catch (Exception e) {
            json.put("code", 103);
            e.printStackTrace();
        }
        return json;
    }
}

package com.whp.usdtfb.user.Impl;

import com.alibaba.fastjson.JSONObject;
import com.whp.usdtfb.user.Dao.FbProblemDao;
import com.whp.usdtfb.user.Interface.FbProblemInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2019/1/4 17:56
 * @descrpition :
 */
@Service
public class FbProblemImpl implements FbProblemInterface {
    @Autowired
    private FbProblemDao fbProblemDao;

    @Override
    public JSONObject FbProblemSelect(Map<String, Object> map) {
        JSONObject json = new JSONObject();
        try {
            List<Map<String, Object>> list = fbProblemDao.FbProblemSelect(map);
            json.put("code", 100);
            json.put("problem", list);
        } catch (Exception e) {
            e.printStackTrace();
            json.put("code", 103);
        }
        return json;
    }

    @Override
    public JSONObject FbProblemDan(String pid) {
        JSONObject json = new JSONObject();
        try {
            Map<String, Object> list = fbProblemDao.FbProblemDan(pid);
            json.put("code", 100);
            json.put("problem", list);
        } catch (Exception e) {
            e.printStackTrace();
            json.put("code", 103);
        }
        return json;
    }

    @Override
    public JSONObject FbProblemInsert(Map<String, Object> map) {
        JSONObject json = new JSONObject();
        try {
            fbProblemDao.FbProblemInsert(map);
            json.put("code", 100);
        } catch (Exception e) {
            e.printStackTrace();
            json.put("code", 103);
        }
        return json;
    }
}

package com.whp.usdtfb.user.Impl;

import com.alibaba.fastjson.JSONObject;
import com.whp.usdtfb.user.Dao.FbProblemClassDao;
import com.whp.usdtfb.user.Interface.FbProblemClassInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2019/1/4 17:53
 * @descrpition :
 */
@Service
public class FbProblemClassImpl implements FbProblemClassInterface {
    @Autowired
    private FbProblemClassDao fbProblemClassDao;

    @Override
    public JSONObject FbProblemSelect() {
        JSONObject json = new JSONObject();
        try {
            List<Map<String, Object>> list = fbProblemClassDao.FbProblemClassSelect();
            json.put("code", 100);
            json.put("ProblemClass", list);
        } catch (Exception e) {
            e.printStackTrace();
            json.put("code", 103);
        }
        return json;
    }
}

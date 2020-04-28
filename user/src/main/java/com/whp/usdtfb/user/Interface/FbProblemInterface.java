package com.whp.usdtfb.user.Interface;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2019/1/4 17:55
 * @descrpition :
 */
public interface FbProblemInterface {

    /**
     * @param map
     * @return
     */
    public JSONObject FbProblemSelect(Map<String, Object> map);

    /**
     * @param map
     * @return
     */
    public JSONObject FbProblemInsert(Map<String, Object> map);

    /**
     * @param pid
     * @return
     */
    public JSONObject FbProblemDan(String pid);
}

package com.whp.usdtfb.user.Interface;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2018/12/18 14:34
 * @descrpition :
 */
public interface FbUserInterface {

    /**
     * @param pid
     * @return
     */
    public JSONObject FbUserSelect(String pid);

    /**
     * @param map
     * @return
     */
    public JSONObject FbUserUpdate(Map<String, Object> map);

    /**
     * @param map
     * @return
     */
    public JSONObject FbUserUpdateQ(Map<String, Object> map);
}

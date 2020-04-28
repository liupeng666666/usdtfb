package com.whp.usdtfb.user.Interface;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2018/12/18 17:11
 * @descrpition :
 */
public interface FbPlayInterface {

    /**
     * @param map
     * @return
     */
    public JSONObject FbPlaySelect(Map<String, Object> map);

    /**
     * @param map
     * @return
     */
    public JSONObject FbPlayInsert(Map<String, Object> map);

    /**
     * @param map
     * @return
     */
    public JSONObject FbPlayUpdate(Map<String, Object> map);

    public JSONObject FbBankSelect();
}

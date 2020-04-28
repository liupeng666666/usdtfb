package com.whp.usdtfb.user.Interface;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2018/12/28 22:00
 * @descrpition :
 */
public interface FbOrderInterface {

    public JSONObject FbOrderInsert(Map<String, Object> map);

    /**
     * 释放订单
     *
     * @param map
     * @return
     */
    public JSONObject FbOrderSf(Map<String, Object> map);

    /**
     * @param map
     * @return
     */
    public JSONObject FbOrderSelect(Map<String, Object> map);

    /**
     * @param map
     * @return
     */
    public JSONObject FbOrderUpdate(Map<String, Object> map);

    /**
     * @param userid
     * @param state
     * @param page
     * @param num
     * @return
     */
    public JSONObject FbOrderSelectQ(String userid, int state, int page, int num);
}

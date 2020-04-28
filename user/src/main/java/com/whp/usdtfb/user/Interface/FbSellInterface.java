package com.whp.usdtfb.user.Interface;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2018/12/26 15:02
 * @descrpition :
 */
public interface FbSellInterface {

    public JSONObject FbSellInsert(Map<String, Object> map);

    /**
     * @param map
     * @return
     */
    public JSONObject FbSellSelect(Map<String, Object> map);

    public JSONObject FbSellDan(String pid, String userid);

    /**
     * @param map
     * @return
     */
    public JSONObject FbSellUpdate(Map<String, Object> map);

    /**
     * @param map
     * @return
     */
    public JSONObject FbSellUpdateState(Map<String, Object> map);
}

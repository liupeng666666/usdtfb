package com.whp.usdtfb.user.Interface;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @author : 张吉玲
 * @data : 2018/12/31 22:37
 * @descrpition :
 */
public interface LFbSellInterface {

    /**
     * @param map
     * @return
     */
    public JSONObject LFbSellSelect(Map<String, Object> map);

}

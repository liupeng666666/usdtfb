package com.whp.usdtfb.user.Interface;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2018/12/24 14:21
 * @descrpition :
 */
public interface FbMoneyInterface {

    /**
     * @param currencyid
     * @return
     */
    public JSONObject FbMoneySelect(String currencyid, String userid, String lock);

    public JSONObject FbMoneyUpdate(Map<String, Object> map);
}

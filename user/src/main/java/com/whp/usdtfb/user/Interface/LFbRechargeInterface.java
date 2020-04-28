package com.whp.usdtfb.user.Interface;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @author : 张吉玲
 * @data : 2019/01/02 22:37
 * @descrpition :
 */
public interface LFbRechargeInterface {

    /**
     * @param userid
     * @return
     */
    public JSONObject LFbRechargeSelect(String userid, Map<String, Object> map);

}

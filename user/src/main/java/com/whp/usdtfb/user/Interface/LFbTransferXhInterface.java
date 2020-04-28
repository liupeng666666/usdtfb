package com.whp.usdtfb.user.Interface;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @author : 张吉玲
 * @data : 2019/01/02 22:37
 * @descrpition :
 */
public interface LFbTransferXhInterface {

    /**
     * @param userid
     * @return
     */
    public JSONObject LFbTransferXhSelect(Map<String, Object> map, String userid);


    /**
     * @param map
     * @param userid
     * @return 资金划转
     */
    public JSONObject FbTransferInsert(Map<String, Object> map, String userid);

}

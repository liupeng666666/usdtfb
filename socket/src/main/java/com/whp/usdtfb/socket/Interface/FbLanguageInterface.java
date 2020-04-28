package com.whp.usdtfb.socket.Interface;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2019/1/3 4:36
 * @descrpition :
 */
public interface FbLanguageInterface {

    public JSONObject FbLanguageSelect(String userid);

    public JSONObject FbLanguageInsert(Map<String, Object> map);
}

package com.whp.usdtfb.user.Interface;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2019/1/2 15:46
 * @descrpition :
 */
public interface FbEvaluateInterface {

    public JSONObject FbEvaluateInsert(Map<String, Object> map);
}

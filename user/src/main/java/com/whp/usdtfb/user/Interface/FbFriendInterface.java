package com.whp.usdtfb.user.Interface;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2019/1/3 13:44
 * @descrpition :
 */
public interface FbFriendInterface {

    public JSONObject FbFriendInsert(Map<String, Object> map);
}

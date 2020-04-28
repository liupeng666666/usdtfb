package com.whp.usdtfb.user.Interface;

import com.alibaba.fastjson.JSONObject;

/**
 * @author : 张吉伟
 * @data : 2018/12/22 22:43
 * @descrpition :
 */
public interface FbCurrencyInterface {

    public JSONObject FbCurrencySelect(String userid);

    public JSONObject FbCurrencyDan(String pid, String currencyid, String fbcurrencyid);
}

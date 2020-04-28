package com.whp.usdtfb.block.Interface;

import com.alibaba.fastjson.JSONObject;

import java.math.BigInteger;

/**
 * @author : 张吉伟
 * @data : 2018/12/17 21:12
 * @descrpition :
 */
public interface EthInterface {

    public JSONObject new_address(String userid);

    public BigInteger getBalance(String accountId);

    public void XunHuan();
}

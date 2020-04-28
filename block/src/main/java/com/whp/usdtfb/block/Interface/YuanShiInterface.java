package com.whp.usdtfb.block.Interface;

import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;

/**
 * @author : 张吉伟
 * @data : 2018/9/4 20:14
 * @descrpition :
 */
public interface YuanShiInterface {

    /**
     * 原始交易
     *
     * @param outValue   转账金额
     * @param username   用户名
     * @param password   密码
     * @param url        连接地址
     * @param address    转账地址
     * @param jy_address 收账地址
     * @param gz_address 手续费，找零地址
     * @param codeid     omni 代号
     * @return
     */
    public JSONObject collectionUsdt(BigDecimal outValue, String username, String password, String url, String address, String jy_address, String gz_address, int codeid);

    /**
     * @param inputCount
     * @return
     */
    public BigDecimal calculationFee(int inputCount);

}

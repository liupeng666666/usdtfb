package com.whp.usdtfb.block.Interface;

import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2018/7/29 21:07
 * @descrpition :
 */
public interface OmniInterface {
    /**
     * 创建usdt 地址
     *
     * @return
     */
    public String getNewAddress();

    /**
     * USDT查询余额
     *
     * @return
     */
    public BigDecimal getBalance(String referenceaddress);

    /**
     * USDT转帐
     *
     * @param addr
     * @param value
     * @return
     */
    public JSONObject send(String addr, BigDecimal value, String z_address);

    /**
     * 验证地址的有效性
     *
     * @param address
     * @return
     * @throws Exception
     */
    public boolean vailedAddress(String address);

    /**
     * 区块链高度
     *
     * @return
     */
    public int getBlockCount();

    /**
     * @return
     */
    public JSONObject getInfo();


    /**
     * 创建代币
     */
    public void create();

    /**
     * 获取所有代币
     */
    public void omni_listproperties();

    /**
     * 查询该地址下所有代币金额
     *
     * @param referenceaddress
     * @return
     */
    public JSONObject getallbalancesforaddress(String referenceaddress);

    /**
     * @param index
     * @param gj_address
     * @return
     */
    public boolean parseBlock(int index, String gj_address, List<Map<String, Object>> list);

    /**
     * @param address
     * @return 比特币钱包余额
     */
    public JSONObject BtcGetbalance(String address);

}

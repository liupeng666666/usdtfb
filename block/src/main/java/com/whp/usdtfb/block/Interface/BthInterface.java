package com.whp.usdtfb.block.Interface;

import java.util.List;
import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2018/7/29 21:07
 * @descrpition :
 */
public interface BthInterface {
    /**
     * @param index
     * @param gj_address
     * @return
     */
    public boolean parseBlock(int index, String gj_address, List<Map<String, Object>> list);

    /**
     * @param address
     * @return
     */
    public boolean vailedAddress(String address);


    /**
     * 区块高度
     *
     * @return
     */
    public int getBlockCount();

    /**
     * 获取地址
     *
     * @return
     */
    public String getNewAddress();


}

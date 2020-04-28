package com.whp.usdtfb.block.Impl;

import client.EosApiRestClient;
import com.alibaba.fastjson.JSONObject;
import com.whp.usdtfb.block.Interface.EosInterface;
import com.whp.usdtfb.block.Utils.EosUtils;
import org.springframework.stereotype.Service;

/**
 * @author : 张吉伟
 * @data : 2019/1/12 21:10
 * @descrpition :
 */
@Service
public class EosImpl implements EosInterface {
    private static EosApiRestClient eosApiRestClient = EosUtils.init();


    @Override
    public JSONObject NewAddress() {
        JSONObject json = new JSONObject();
        try {
            String address = eosApiRestClient.createWallet("123");
            json.put("code", 100);
            json.put("address", address);
        } catch (Exception e) {
            e.printStackTrace();
            json.put("code", 103);
        }
        return null;
    }
}

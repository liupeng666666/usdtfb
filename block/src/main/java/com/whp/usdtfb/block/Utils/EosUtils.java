package com.whp.usdtfb.block.Utils;

import client.EosApiClientFactory;
import client.EosApiRestClient;

/**
 * @author : 张吉伟
 * @data : 2019/1/12 21:11
 * @descrpition :
 */
public class EosUtils {

    private static String walletUrl = "http://47.91.217.242";//本地启动的wallet
    private static String chainUrl = "https://api-v2.eosasia.one";
    static EosApiRestClient eosApiRestClient = null;

    public static EosApiRestClient init() {
        eosApiRestClient = EosApiClientFactory.newInstance(walletUrl).newRestClient();
        return eosApiRestClient;
    }
}

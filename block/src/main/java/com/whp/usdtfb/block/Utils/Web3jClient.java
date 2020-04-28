package com.whp.usdtfb.block.Utils;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.http.HttpService;

/**
 * @author : 张吉伟
 * @data : 2018/12/17 21:04
 * @descrpition :
 */
public class Web3jClient {

    private static String ip = "http://47.91.217.242:9212/";

    private Web3jClient() {
    }

    private volatile static Web3j web3j;
    private volatile static Admin admin;

    public static Web3j getClient() {

        if (web3j == null) {
            synchronized (Web3jClient.class) {
                if (web3j == null) {
                    web3j = Web3j.build(new HttpService(ip));
                }
            }
        }
        return web3j;
    }

    public static Admin getAdmin() {
        if (admin == null) {
            synchronized (Web3jClient.class) {
                if (admin == null) {
                    admin = Admin.build(new HttpService(ip));
                }
            }
        }
        return admin;
    }

}

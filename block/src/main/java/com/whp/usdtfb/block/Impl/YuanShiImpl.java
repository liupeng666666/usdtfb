package com.whp.usdtfb.block.Impl;

import com.alibaba.fastjson.JSONObject;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import com.whp.usdtfb.block.Interface.YuanShiInterface;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2018/9/4 20:14
 * @descrpition :
 */
@Service
public class YuanShiImpl implements YuanShiInterface {

    @Override
    public JSONObject collectionUsdt(BigDecimal outValue, String username, String password, String url, String address, String jy_address, String gz_address, int codeid) {
        System.out.println("outvalue:" + outValue);
        System.out.println("address:" + address);
        System.out.println("jy_address:" + jy_address);
        System.out.println("gz_address:" + gz_address);
        JsonRpcHttpClient client = null;
        JSONObject json = new JSONObject();
        try {
            Map[] argsOne = new Map[2];
            Map[] args1 = new Map[2];
            String creb = Base64.encodeBase64String((username + ":" + password).getBytes());
            Map<String, String> headers = new HashMap<>(2);
            headers.put("Authorization", "Basic " + creb);
            headers.put("server", "1");
            client = new JsonRpcHttpClient(new URL(url), headers);
            List<Map> listunspent = (List<Map>) client.invoke("listunspent", new Object[]{0, 99999, new String[]{address}}, Object.class);
            System.out.println("listunspent:" + listunspent);
            for (Map map : listunspent) {
                double amount = (double) map.get("amount");
                //这里是找一笔uxto的btc交易做桥梁
                if (amount > 0.000005) {
                    System.out.println("amount:" + amount);
                    String txId = (String) map.get("txid");
                    String scriptPubKey = (String) map.get("scriptPubKey");
                    int vout = (int) map.get("vout");
                    Map input = new HashMap<>();
                    input.put("txid", txId);
                    input.put("vout", vout);
                    args1[0] = input;
                    Map inputT = new HashMap<>();
                    inputT.put("txid", txId);
                    inputT.put("vout", vout);
                    inputT.put("scriptPubKey", scriptPubKey);
                    inputT.put("value", amount);
                    argsOne[0] = inputT;
                    break;
                }
            }
            List<Map> btc = (List<Map>) client.invoke("listunspent", new Object[]{0, 99999, new String[]{gz_address}}, Object.class);
            for (Map map : btc) {
                double amount = (double) map.get("amount");
                //这里是找一笔uxto的btc交易做桥梁
                if (amount > 0.000005) {
                    String txId = (String) map.get("txid");
                    int vout = (int) map.get("vout");
                    String scriptPubKey = (String) map.get("scriptPubKey");
                    Map input = new HashMap<>();
                    input.put("txid", txId);
                    input.put("vout", vout);
                    args1[1] = input;
                    Map inputT = new HashMap<>();
                    inputT.put("txid", txId);
                    inputT.put("vout", vout);
                    inputT.put("scriptPubKey", scriptPubKey);
                    inputT.put("value", amount);
                    argsOne[1] = inputT;
                    break;
                }
            }
            //计算字节大小和费用(因为是归集USDT 所以我用最小的输入来降低手续费，如果你是BTC和USDT一起归总那就要根据归集的输入来计算了)
//            BigDecimal keyCount = calculationFee(1);
//            //将聪换算成BTC
//            BigDecimal transferFee = keyCount.divide(new BigDecimal("100000000"), 8, RoundingMode.HALF_UP);
//            if (transferFee.compareTo(BigDecimal.ZERO) <= 0 || outValue.compareTo(transferFee) <= 0) {
//                json.put("code",105);
//                return json;
//            }
            BigDecimal transferFee = BigDecimal.valueOf(0.0005);
            /**
             * 通过全节点构造原生交易
             */
            //创建BTC交易
            Map args2 = new HashMap<>();
            System.out.println(args1);
            Object result = (Object) client.invoke("createrawtransaction", new Object[]{args1, args2}, Object.class);
            String transaction = String.valueOf(result);
            /*//解锁钱包
            client.invoke("walletpassphrase", new Object[]{"xxxx", 100}, Object.class);*/
            //创建Usdt交易
            String simplesendResult = (String) client.invoke("omni_createpayload_simplesend", new Object[]{codeid, outValue.toString()}, Object.class);
            //usdt交易附加到BTC交易上
            String opreturnResult = (String) client.invoke("omni_createrawtx_opreturn", new Object[]{transaction, simplesendResult}, Object.class);
            //设置归总地址
            String reference = (String) client.invoke("omni_createrawtx_reference", new Object[]{opreturnResult, jy_address}, Object.class);
            //填写手续费及找零地址
            String changeResult = (String) client.invoke("omni_createrawtx_change", new Object[]{reference, argsOne, gz_address, transferFee}, Object.class);
            //获取原生交易hex
            Map signrawtransaction = (Map) client.invoke("signrawtransaction", new Object[]{changeResult}, Object.class);
            System.out.println("hex:" + signrawtransaction.get("hex"));
            //广播交易
            String txId = (String) client.invoke("sendrawtransaction", new Object[]{signrawtransaction.get("hex")}, Object.class);
            System.out.println("txid:" + txId);
            json.put("code", 100);
            json.put("txid", txId);
            //json.put("zr_wallet",)
            return json;
        } catch (Exception e) {
            json.put("code", 103);
            e.printStackTrace();
        } catch (Throwable e1) {
            json.put("code", 103);
            e1.printStackTrace();
        }
        return json;
    }

    /**
     * 计算手续费
     *
     * @param inputCount
     * @return
     */
    @Override
    public BigDecimal calculationFee(int inputCount) {
        //计算手续费获取每个字节的手续费
        String url = "https://bitcoinfees.earn.com/api/v1/fees/recommended";
        //计算字节大小和费用
        String resut = sendGet(url, null);
        //=====resut===>转对象Model省略了，其实http请求都有公用的方法所以我随便写了。。
        JSONObject Model = JSONObject.parseObject(resut);
        System.out.println("model:" + Model);
        BigDecimal keyCount = BigDecimal.valueOf((inputCount * 148 + 44) * Model.getInteger("halfHourFee"));
        return keyCount;

    }

    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            result = in.readLine();

            System.out.println("=========" + result);
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

}

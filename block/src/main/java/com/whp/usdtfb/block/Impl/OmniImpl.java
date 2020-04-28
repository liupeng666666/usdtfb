package com.whp.usdtfb.block.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.whp.usdtfb.Util.utils.RedisUtils;
import com.whp.usdtfb.block.Dao.FbUsdtDao;
import com.whp.usdtfb.block.Interface.OmniInterface;
import com.whp.usdtfb.block.Interface.YuanShiInterface;
import com.whp.usdtfb.block.Utils.HttpUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2018/7/29 21:07
 * @descrpition :
 */
@Service
public class OmniImpl implements OmniInterface {
    @Autowired
    private YuanShiInterface yuanShiInterface;
    @Autowired
    private FbUsdtDao fbUsdtDao;
    // private String address = "n2qnt1Qr3N5amBx4WvfcALf4nTb3Qj6WhQ";
    //private String address = "1MipTYmNCd2c36kphcNMtM1o9dUAXhKKPQ";
    private String address = "n3n8Buu5QDZYFoaPUdw4HZ7ANEKySoUX2W";
    private long propertyid = 695;
    // private String url = "http://192.168.5.199:2181";
    private String url = "http://47.91.217.242:2181";
    private String username = "usdt@bitcoin";
    private String password = "P@ssw0rd@2018";


    private final static String RESULT = "result";
    private final static String METHOD_SEND_TO_ADDRESS = "omni_send";

    private final static String METHOD_GET_TRANSACTION = "omni_gettransaction";
    private final static String METHOD_GET_BLOCK_COUNT = "getblockcount";
    private final static String METHOD_NEW_ADDRESS = "getnewaddress";
    private final static String METHOD_GET_BALANCE = "omni_getbalance";
    private final static String METHOD_GET_LISTBLOCKTRANSACTIONS = "omni_listblocktransactions";
    private static int omni_height = 0;


    @Override
    public JSONObject getInfo() {
        return doRequest("omni_getinfo");
    }

    @Override
    public void create() {
        //   JSONObject json=doRequest("omni_createpayload_issuancefixed",1,1,0,"bcurrency","","bru","","","9000000");
        JSONObject json = doRequest("omni_sendissuancefixed", "1AYhkCrUPEcEMpKmskUNaQv8wUfdC4ZUcb", 1, 2, 0, "Digital asset trading platform", "transaction payment", "BRU", "http://b-currency.top", "BRU  Token is a negotiable encrypted digital rights certificate, developed by REVOLTEK primarily for trading platforms and certified investors, that circulates payments. A token of an asset offered to a certified investor.", "90000000");
        System.out.println("json:" + json);
    }

    @Override
    public void omni_listproperties() {
        JSONObject json = doRequest("omni_listproperties");
        System.out.println("json:" + json);
    }


    @Override
    public String getNewAddress() {
        JSONObject json = doRequest(METHOD_NEW_ADDRESS);
        if (isError(json)) {
            // System.out.println("获取USDT地址失败:{}" + json.get("error"));
            return "";
        }
        return json.getString(RESULT);

    }

    @Override
    public JSONObject BtcGetbalance(String address) {
        JSONObject json = new JSONObject();
        return json;
    }

    @Override
    public BigDecimal getBalance(String referenceaddress) {
        JSONObject json = doRequest(METHOD_GET_BALANCE, referenceaddress, propertyid);
        System.out.println("json:" + json);
        if (isError(json)) {
            // System.out.println("获取USDT余额:{}" + json.get("error"));
            return BigDecimal.ZERO;
        }
        return json.getJSONObject(RESULT).getBigDecimal("balance");

    }

    @Override
    public JSONObject getallbalancesforaddress(String referenceaddress) {
        JSONObject json = doRequest("omni_getallbalancesforaddress", referenceaddress);
        System.out.println("json:" + json);
        return json;
    }

    @Override
    public JSONObject send(String addr, BigDecimal value, String z_address) {
        if (vailedAddress(addr)) {
            JSONObject json = doRequest(METHOD_SEND_TO_ADDRESS, z_address, addr, propertyid, value);
            return json;
        }
        return null;
    }

    @Override
    public boolean vailedAddress(String address) {
        JSONObject json = doRequest("validateaddress", address);
        if (isError(json)) {
            //  System.out.println("USDT验证地址失败:" + json.get("error"));
            return false;
        } else {
            return json.getJSONObject(RESULT).getBoolean("isvalid");
        }

    }

    @Override
    public int getBlockCount() {
        JSONObject json = null;
        try {
            json = doRequest(METHOD_GET_BLOCK_COUNT);
            if (!isError(json)) {
                return json.getInteger("result");
            } else {
                // System.out.println(json.toString());
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }


    private JSONObject doRequest(String method, Object... params) {
        JSONObject param = new JSONObject();
        param.put("id", System.currentTimeMillis() + "");
        param.put("jsonrpc", "2.0");
        param.put("method", method);
        if (params != null) {
            param.put("params", params);
        }
        String creb = Base64.encodeBase64String((username + ":" + password).getBytes());
        Map<String, String> headers = new HashMap<>(2);
        headers.put("Authorization", "Basic " + creb);
        String resp = "";
        if (METHOD_GET_TRANSACTION.equals(method)) {
            try {
                resp = HttpUtil.jsonPost(url, headers, param.toJSONString());
                //resp= HttpClientUtils.httpPost(url,param,creb).toString();
            } catch (Exception e) {
                if (e instanceof IOException) {
                    resp = "{}";
                }
            }
        } else {
            // System.out.println("url:" + url);
            //  System.out.println("param:" + param.toJSONString());
            //  System.out.println("headers:" + headers);
            resp = HttpUtil.jsonPost(url, headers, param.toJSONString());
            //resp= HttpClientUtils.httpPost(url,param,creb).toString();
        }
        return JSON.parseObject(resp);
    }

    private boolean isError(JSONObject json) {
        if (json == null || (StringUtils.isNotEmpty(json.getString("error")) && json.get("error") != "null")) {
            return true;
        }
        return false;
    }

    @Override
    public boolean parseBlock(int index, String gj_address, List<Map<String, Object>> list) {
        try {
            JSONObject jsonBlock = doRequest(METHOD_GET_LISTBLOCKTRANSACTIONS, index);

            System.out.println("jsonBlock:" + jsonBlock);
            if (isError(jsonBlock)) {
                System.out.println("访问USDT出错");
                return false;
            }
            JSONArray jsonArrayTx = jsonBlock.getJSONArray(RESULT);
            if (jsonArrayTx == null || jsonArrayTx.size() == 0) {
                //没有交易
                if (index <= omni_height + 1) {
                    omni_height = index;
                    RedisUtils.set("omni.height", index + "", 7);
                }
                return true;
            }
            Iterator<Object> iteratorTxs = jsonArrayTx.iterator();
            while (iteratorTxs.hasNext()) {
                String txid = (String) iteratorTxs.next();
                boolean status = parseTx(txid, index, gj_address, list);
                System.out.println("status:" + status);
                if (status == false) {
                    //请求redis 修改高度
                    System.out.println("index:" + index + ",omni_height:" + omni_height);
                    if (index != 0) {
                        if (index <= omni_height + 1) {
                            omni_height = index;
                            RedisUtils.set("omni.height", index + "", 7);
                        } else {
                            if (omni_height == 0) {
                                omni_height = index;
                            }
                        }
                    } else {
                        omni_height = index;
                        RedisUtils.set("omni.height", index + "", 7);
                    }
                } else {
                    omni_height = index - 2;
                    RedisUtils.set("omni.height", (index - 2) + "", 7);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean parseTx(String txid, int index, String gj_address, List<Map<String, Object>> list) {
        JSONObject jsonTransaction = doRequest(METHOD_GET_TRANSACTION, txid);
        if (isError(jsonTransaction)) {
            return false;
        }
        JSONObject jsonTResult = jsonTransaction.getJSONObject(RESULT);
        if (!jsonTResult.getBoolean("valid")) {
            return false;
        }
        int propertyidResult = jsonTResult.getIntValue("propertyid");
        if (propertyidResult != propertyid) {
            return false;
        }
        double value = jsonTResult.getDouble("amount");
        if (value > 0) {
            String address = jsonTResult.getString("referenceaddress");
            for (Map<String, Object> addressModel : list) {
                //如果有地址是分配给用记的地址， 则说明用户在充值
                if (address.equals(addressModel.get("address"))) {
                    //查询余额 如果有充值则添加到记录中。并转移走
                    System.out.println("============================");
                    BigDecimal surplus = getBalance(address);
                    System.out.println("============================:" + surplus);
                    if (surplus.compareTo(BigDecimal.valueOf(value)) == -1) {
                        return false;
                    } else {
                        Map<String, Object> map = fbUsdtDao.FbUsdtDan(address);
                        if (map == null) {
                            //转移金钱，插入数据库
                            JSONObject json = yuanShiInterface.collectionUsdt(BigDecimal.valueOf(value), username, password, url, address, gj_address, gj_address, propertyidResult);
                            if (json.getInteger("code") != 100) {
                                return true;
                            } else {

                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("from_address", jsonTResult.getString("sendingaddress"));
                                jsonObject.put("userid", addressModel.get("userid"));
                                jsonObject.put("to_address", address);
                                jsonObject.put("txid", json.get("txid"));
                                jsonObject.put("money", BigDecimal.valueOf(value));
                                jsonObject.put("height", index);
                                jsonObject.put("codeid", propertyidResult);
                                fbUsdtDao.FbUsdtInsert(jsonObject);
                                return false;
                            }

                        } else if (Integer.parseInt(map.get("height").toString()) >= index) {
                            return false;
                        } else {
                            //转移金钱，插入数据库
                            JSONObject json = yuanShiInterface.collectionUsdt(BigDecimal.valueOf(value), username, password, url, address, gj_address, gj_address, propertyidResult);
                            if (json.getInteger("code") != 100) {
                                return true;
                            } else {
                                //插入数据库
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("from_address", jsonTResult.getString("sendingaddress"));
                                jsonObject.put("userid", addressModel.get("userid"));
                                jsonObject.put("to_address", address);
                                jsonObject.put("txid", json.get("txid"));
                                jsonObject.put("money", BigDecimal.valueOf(value));
                                jsonObject.put("height", index);
                                jsonObject.put("codeid", propertyidResult);
                                fbUsdtDao.FbUsdtInsert(jsonObject);
                                return false;
                            }
                        }
                    }
                    //结束

                }
            }
        } else {
            return false;
        }
        return false;
    }
}

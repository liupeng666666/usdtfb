package com.whp.usdtfb.block.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.whp.usdtfb.Util.utils.RedisUtils;
import com.whp.usdtfb.block.Dao.FbUsdtDao;
import com.whp.usdtfb.block.Interface.BtcInterface;
import com.whp.usdtfb.block.Interface.OmniInterface;
import com.whp.usdtfb.block.Interface.YuanShiInterface;
import com.whp.usdtfb.block.Utils.HttpUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
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
public class BtcImpl implements BtcInterface {
    @Autowired
    private YuanShiInterface yuanShiInterface;
    @Autowired
    private FbUsdtDao fbUsdtDao;

    private String url = "http://47.91.217.242:2181";
    private String username = "usdt@bitcoin";
    private String password = "P@ssw0rd@2018";


    private final static String RESULT = "result";
    private final static String METHOD_SEND_TO_ADDRESS = "sendtoaddress";
    private final static String METHOD_GET_BLOCK = "getblock";
    private final static String METHOD_GET_BLOCK_HASH = "getblockhash";
    private final static String METHOD_GET_TRANSACTION = "gettransaction";
    private final static String METHOD_GET_BLOCK_COUNT = "getblockcount";
    private final static String METHOD_NEW_ADDRESS = "getnewaddress";
    private final static String METHOD_GET_BALANCE = "getbalance";
    private final static int MIN_CONFIRMATION = 6;
    private static int omni_height = 0;


    /**
     * 区块高度
     *
     * @return
     */
    @Override
    public int getBlockCount() {
        JSONObject json = null;
        try {
            json = doRequest(METHOD_GET_BLOCK_COUNT);
            if (!isError(json)) {
                return json.getInteger("result");
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    /**
     * 验证地址的有效性
     *
     * @param address
     * @return
     * @throws Exception
     */
    @Override
    public boolean vailedAddress(String address) {
        JSONObject json = doRequest("validateaddress", address);
        if (isError(json)) {
            return false;
        } else {
            return json.getJSONObject(RESULT).getBoolean("isvalid");
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
            } catch (Exception e) {
                if (e instanceof IOException) {
                    resp = "{}";
                }
            }
        } else {
            resp = HttpUtil.jsonPost(url, headers, param.toJSONString());
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
            JSONObject jsonBlockHash = doRequest(METHOD_GET_BLOCK_HASH, index);
            if (isError(jsonBlockHash)) {

                return false;
            }
            String hash = jsonBlockHash.getString(RESULT);
            JSONObject jsonBlock = doRequest(METHOD_GET_BLOCK, hash);
            if (isError(jsonBlock)) {
                return false;
            }
            JSONObject jsonBlockResult = jsonBlock.getJSONObject(RESULT);
            int confirm = jsonBlockResult.getInteger("confirmations");
            System.out.println("confirm:" + confirm);
            if (confirm >= MIN_CONFIRMATION) {
                JSONArray jsonArrayTx = jsonBlockResult.getJSONArray("tx");
                System.out.println("数量：" + jsonArrayTx.size());
                if (jsonArrayTx == null || jsonArrayTx.size() == 0) {
                    //没有交易
                    RedisUtils.set("BTC", index + "", 7);
                    return true;
                }

                Iterator<Object> iteratorTxs = jsonArrayTx.iterator();
                int i = 0;
                while (iteratorTxs.hasNext()) {
                    i += 1;
                    System.out.println("i:" + i);
                    String txid = (String) iteratorTxs.next();
                    boolean status = parseTx(txid, index, gj_address, list);
                    if (status == false) {
                        if (index != 0) {
                            if (index <= omni_height + 1) {
                                omni_height = index;
                                RedisUtils.set("BTC", index + "", 7);
                            } else {
                                if (omni_height == 0) {
                                    omni_height = index;
                                }
                            }
                        } else {
                            omni_height = index;
                            RedisUtils.set("BTC", index + "", 7);
                        }
                    } else {
                        omni_height = index - 2;
                        RedisUtils.set("BTC", (index - 2) + "", 7);
                    }
                }
                return true;
            } else {
                return false;
            }

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
        JSONObject jsonTransactionResult = jsonTransaction.getJSONObject(RESULT);
        JSONArray jsonArrayVout = jsonTransactionResult.getJSONArray("details");
        if (jsonArrayVout == null || jsonArrayVout.size() == 0) {
            return false;
        }
        System.out.println("size:" + jsonArrayVout.size());
        Iterator<Object> iteratorVout = jsonArrayVout.iterator();
        JSONArray array = new JSONArray();
        while (iteratorVout.hasNext()) {
            JSONObject jsonVout = (JSONObject) iteratorVout.next();
            double value = jsonVout.getDouble("amount");
            if (jsonVout.containsKey("address")) {
                array.add(jsonVout.getString("address"));
            }
            if (value > 0) {
                String address = jsonVout.getString("address");
                for (Map<String, Object> addressModel : list) {
                    //如果有地址是分配给用记的地址， 则说明用户在充值
                    if (address.equals(addressModel.get("address"))) {
                        //查询余额 如果有充值则添加到记录中。并转移走
                        Map<String, Object> map = fbUsdtDao.FbUsdtDan(address);
                        array.remove(address);
                        String from_address = null;
                        if (array.size() > 0) {
                            from_address = array.get(0).toString();
                        }
                        if (map == null) {

                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("from_address", from_address);
                            jsonObject.put("userid", addressModel.get("userid"));
                            jsonObject.put("to_address", address);
                            jsonObject.put("txid", txid);
                            jsonObject.put("money", BigDecimal.valueOf(value));
                            jsonObject.put("height", index);
                            jsonObject.put("codeid", 1);
                            fbUsdtDao.FbUsdtInsert(jsonObject);
                            return false;

                        } else if (Integer.parseInt(map.get("height").toString()) >= index) {
                            return false;
                        } else {
                            //插入数据库
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("from_address", from_address);
                            jsonObject.put("userid", addressModel.get("userid"));
                            jsonObject.put("to_address", address);
                            jsonObject.put("txid", txid);
                            jsonObject.put("money", BigDecimal.valueOf(value));
                            jsonObject.put("height", index);
                            jsonObject.put("codeid", 1);
                            fbUsdtDao.FbUsdtInsert(jsonObject);
                            return false;

                        }
                    }
                    //结束
                }
            }
        }

        return false;
    }
}

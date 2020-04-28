package com.whp.usdtfb.user.Impl;

import com.alibaba.fastjson.JSONObject;
import com.whp.usdtfb.user.Dao.FbCurrencyDao;
import com.whp.usdtfb.user.Dao.FbMoneyDao;
import com.whp.usdtfb.user.Dao.LFbAssetOrderDao;
import com.whp.usdtfb.user.Dao.LFbTransferXhDao;
import com.whp.usdtfb.user.Interface.LFbTransferXhInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author : 张吉玲
 * @data : 2019/01/02 22:28
 * @descrpition :
 */
@Service
public class LFbTransferXhImpl implements LFbTransferXhInterface {

    @Autowired
    private LFbTransferXhDao lFbTransferXhDao;

    @Autowired
    private FbCurrencyDao fbCurrencyDao;
    @Autowired
    private FbMoneyDao fbMoneyDao;

    @Autowired
    private LFbAssetOrderDao lFbAssetOrderDao;


    @Override
    public JSONObject LFbTransferXhSelect(Map<String, Object> map, String userid) {
        JSONObject json = new JSONObject();

        try {

            // 现货划转情报取得
            List<Map<String, Object>> transferList = lFbTransferXhDao.LFbTransferXhSelect(map, userid);

            json.put("transferList", transferList);
            json.put("code", 100);

        } catch (Exception e) {
            e.printStackTrace();
            json.put("code", 103);
        }
        return json;
    }

    @Override
    public JSONObject FbTransferInsert(Map<String, Object> map, String userid) {
        JSONObject json = new JSONObject();
        try {
            JSONObject jsonObject = new JSONObject(map);
            if ("BRU".equals(jsonObject.get("name")) || "USDT".equals(jsonObject.get("name"))) {
                Map<String, Object> fmap = fbCurrencyDao.FbCurrencyDan(jsonObject.getString("name"));
                if (fmap == null) {
                    json.put("code", 104);
                } else {
                    if (jsonObject.getInteger("state") == 1) {
                        Map<String, Object> zmap = fbMoneyDao.FbMoneySelect(fmap.get("pid").toString(), userid, null);
                        if (zmap == null) {
                            json.put("code", 104);
                        } else {
                            JSONObject jsonObject1 = new JSONObject(zmap);
                            if (jsonObject1.getBigDecimal("surplus").compareTo(jsonObject.getBigDecimal("money")) == -1) {
                                json.put("code", 105);
                            } else {
                                JSONObject cjson = new JSONObject();
                                cjson.put("currencyid", fmap.get("pid").toString());
                                cjson.put("userid", userid);
                                cjson.put("surplus", jsonObject.getBigDecimal("money"));
                                cjson.put("type", "1");
                                Map<String, Object> xmap = fbMoneyDao.FbMoneySelect(fmap.get("pid").toString(), userid, null);
                                if (xmap == null) {
                                    fbMoneyDao.FbMoneyInsert(fmap.get("pid").toString(), userid);
                                }
                                fbMoneyDao.FbMoneyUpdate(cjson);
                                JSONObject cjson1 = new JSONObject();
                                cjson1.put("currencyToName", jsonObject.getString("name"));
                                cjson1.put("surplusAdd", jsonObject.getBigDecimal("money"));
                                cjson1.put("userid", userid);
                                lFbAssetOrderDao.LFbAssetOrderUpd4(cjson1, userid);
                                lFbAssetOrderDao.LFbAssetTransferIns(fmap.get("pid").toString(), jsonObject.getString("name"), jsonObject.getBigDecimal("money"), jsonObject.getBigDecimal("money"), userid, jsonObject.getString("state"));
                                json.put("code", 100);
                            }
                        }
                    } else {
                        Map<String, Object> zmap = lFbAssetOrderDao.LFbAssetOrderUsdtSelect(userid);
                        if (zmap == null) {
                            json.put("code", 104);
                        } else {
                            JSONObject jsonObject1 = new JSONObject(zmap);
                            if ("USDT".equals(jsonObject.get("name"))) {
                                if (jsonObject1.getBigDecimal("surplus").compareTo(jsonObject.getBigDecimal("money")) == -1) {
                                    json.put("code", 105);
                                    return json;
                                }
                            } else {
                                if (jsonObject1.getBigDecimal("bur_money").compareTo(jsonObject.getBigDecimal("money")) == -1) {
                                    json.put("code", 105);
                                    return json;
                                }
                            }
                            JSONObject cjson1 = new JSONObject();
                            cjson1.put("currencyFrmName", jsonObject.getString("name"));
                            cjson1.put("surplusUpd", jsonObject.getBigDecimal("money"));
                            cjson1.put("userid", userid);
                            lFbAssetOrderDao.LFbAssetOrderUpd3(cjson1, userid);
                            JSONObject cjson = new JSONObject();
                            cjson.put("currencyid", fmap.get("pid").toString());
                            cjson.put("userid", userid);
                            cjson.put("surplus", BigDecimal.ZERO.subtract(jsonObject.getBigDecimal("money")));
                            cjson.put("type", "1");
                            Map<String, Object> xmap = fbMoneyDao.FbMoneySelect(fmap.get("pid").toString(), userid, null);
                            if (xmap == null) {
                                fbMoneyDao.FbMoneyInsert(fmap.get("pid").toString(), userid);
                            }
                            fbMoneyDao.FbMoneyUpdate(cjson);

                            lFbAssetOrderDao.LFbAssetTransferIns(jsonObject.getString("name"), fmap.get("pid").toString(), jsonObject.getBigDecimal("money"), jsonObject.getBigDecimal("money"), userid, jsonObject.getString("state"));
                            json.put("code", 100);
                        }
                    }
                }

            } else {
                json.put("code", 104);
            }

        } catch (Exception e) {
            e.printStackTrace();
            json.put("code", 103);
        }
        return json;
    }
}

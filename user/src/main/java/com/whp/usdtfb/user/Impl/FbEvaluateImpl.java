package com.whp.usdtfb.user.Impl;

import com.alibaba.fastjson.JSONObject;
import com.whp.usdtfb.Util.utils.RedisUtils;
import com.whp.usdtfb.user.Dao.FbEvaluateDao;
import com.whp.usdtfb.user.Dao.FbOrderDao;
import com.whp.usdtfb.user.Dao.FbSellDao;
import com.whp.usdtfb.user.Interface.FbEvaluateInterface;
import com.whp.usdtfb.user.Interface.FbMoneyInterface;
import com.whp.usdtfb.user.Interface.FbUserInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2019/1/2 15:46
 * @descrpition :
 */
@Service
public class FbEvaluateImpl implements FbEvaluateInterface {
    @Autowired
    private FbEvaluateDao fbEvaluateDao;
    @Autowired
    private FbOrderDao fbOrderDao;
    @Autowired
    private FbUserInterface fbUserInterface;
    @Autowired
    private FbSellDao fbSellDao;
    @Autowired
    private FbMoneyInterface fbMoneyInterface;

    @Override
    public JSONObject FbEvaluateInsert(Map<String, Object> map) {
        JSONObject json = new JSONObject();
        try {
            Map<String, Object> fmap = fbOrderDao.FbOrderDan(map);
            if (fmap != null) {
                int state = 0;
                JSONObject jsonObject = new JSONObject(fmap);
                Map<String, Object> maps = new HashMap<>();
                if ("1".equals(map.get("type"))) {
                    maps.put("userid", jsonObject.get("userid"));
                    maps.put("puserid", jsonObject.get("fuserid"));
                    if (jsonObject.getInteger("state") == 3) {
                        state = 9;

                    }
                    if (jsonObject.getInteger("state") == 8) {
                        state = 7;
                    }
                } else {
                    maps.put("userid", jsonObject.get("fuserid"));
                    maps.put("puserid", jsonObject.get("userid"));
                    if (jsonObject.getInteger("state") == 3) {
                        state = 8;

                    }
                    if (jsonObject.getInteger("state") == 9) {
                        state = 7;
                    }
                }
                maps.put("type", jsonObject.get("type"));
                maps.put("orderid", jsonObject.get("pid"));
                maps.put("score", map.get("score"));
                maps.put("reputation", map.get("score"));
                if (state != 0) {
                    fbEvaluateDao.FbEvakyateInsert(maps);
                    fbUserInterface.FbUserUpdateQ(maps);
                    Map<String, Object> cmap = new HashMap<>();
                    cmap.put("state", state);
                    cmap.put("pid", jsonObject.get("pid"));
                    fbOrderDao.FbOrderUpdate(cmap);
                    String value = RedisUtils.get(map.get("pid").toString(), 5);
                    if (value != null && value != "") {
                        JSONObject fjson = JSONObject.parseObject(value);
                        JSONObject cjson = fjson.getJSONObject("cjson");
                        JSONObject ffjson = fjson.getJSONObject("fjson");
                        cjson.put("state", state);
                        fjson.put("cjson", cjson);
                        long datetime = new Date().getTime();
                        long time = Long.parseLong(ffjson.getString("time")) * 60;
                        datetime = datetime + time * 1000;
                        fjson.put("datetime", datetime);
                        RedisUtils.del(map.get("pid").toString(), 5);
                        RedisUtils.time_set(map.get("pid").toString(), fjson.toString(), 5, time);
                    }

                    int count = fbOrderDao.FbOrderJx(jsonObject.getString("sellid"));
                    if (count == 0) {
                        Map<String, Object> smap = fbSellDao.FbSellDan(jsonObject.getString("sellid"));
                        if (smap != null && smap.containsKey("surplus") && smap.containsKey("min_money")) {
                            JSONObject sjson = new JSONObject(smap);
                            String cny = RedisUtils.get("CNY", 4);
                            String fvalue = RedisUtils.get("kline." + sjson.get("currencyid"), 0);
                            if (fvalue != null && fvalue != "") {
                                JSONObject jsonObject1 = JSONObject.parseObject(fvalue);
                                BigDecimal bigDecimal = sjson.getBigDecimal("surplus").multiply(jsonObject1.getBigDecimal("close")).multiply(new BigDecimal(cny));

                                if (sjson.getBigDecimal("min_money").compareTo(bigDecimal) != -1) {
                                    //释放
                                    JSONObject cjson = new JSONObject();
                                    cjson.put("pid", jsonObject.getString("sellid"));
                                    cjson.put("state", 2);
                                    fbSellDao.FbSellStateUpdate(cjson);
                                    if (sjson.getInteger("type") == 1) {
                                        Map<String, Object> cmap1 = new HashMap<>();
                                        cmap1.put("currencyid", sjson.get("fb_currency_id"));
                                        cmap1.put("userid", sjson.get("userid"));
                                        cmap1.put("surplus", BigDecimal.ZERO.subtract(sjson.getBigDecimal("surplus")));
                                        fbMoneyInterface.FbMoneyUpdate(cmap1);
                                    }

                                }

                            }
                        }

                    }
                    json.put("code", 100);
                } else {
                    json.put("code", 104);
                }
            } else {
                json.put("code", 102);
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.put("code", 103);
        }

        return json;
    }
}

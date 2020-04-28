package com.whp.usdtfb.user.Impl;

import com.alibaba.fastjson.JSONObject;
import com.whp.usdtfb.Util.utils.MD5Util;
import com.whp.usdtfb.Util.utils.RedisUtils;
import com.whp.usdtfb.user.Dao.*;
import com.whp.usdtfb.user.Interface.*;
import com.whp.usdtfb.user.utils.Sms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author : 张吉伟
 * @data : 2018/12/28 22:02
 * @descrpition :
 */
@Service
public class FbOrderImpl implements FbOrderInterface {

    @Autowired
    private FbOrderDao fbOrderDao;
    @Autowired
    private FbMoneyInterface fbMoneyInterface;
    @Autowired
    private FbSellInterface fbSellInterface;
    @Autowired
    private FbSellDao fbSellDao;
    @Autowired
    private FbUserInterface fbUserInterface;
    @Autowired
    private FbPlayInterface fbPlayInterface;

    @Autowired
    private FbScoreDao fbScoreDao;
    @Autowired
    private FbEvaluateDao fbEvaluateDao;
    @Autowired
    private SubUserDao subUserDao;
    @Autowired
    private FbPoundageLogDao fbPoundageLogDao;


    @Override
    public JSONObject FbOrderInsert(Map<String, Object> map) {
        JSONObject json = new JSONObject();
        String uuid = UUID.randomUUID().toString();
        map.put("pid", uuid);
        if (map.containsKey("type") && map.containsKey("userid") && map.containsKey("fuserid") && map.containsKey("number") && map.containsKey("sellid")) {
            JSONObject cjson = new JSONObject(map);
            JSONObject fjson = fbSellInterface.FbSellDan(cjson.getString("sellid"), cjson.getString("fuserid"));

            if (fjson.getInteger("code") == 100) {
                if (cjson.getInteger("type") == 0) {
                    JSONObject sell_json = fjson.getJSONObject("sell");
                    if (sell_json.containsKey("surplus")) {
                        if (cjson.getBigDecimal("number").compareTo(sell_json.getBigDecimal("surplus")) != 1) {
                            int code = tijiao(cjson, map, uuid, fjson, 0);
                            json.put("code", code);
                            json.put("uuid", uuid);
                        } else {
                            json.put("code", 104);
                        }
                    } else {
                        json.put("code", 102);
                    }
                } else {
                    JSONObject money_json = fjson.getJSONObject("money");
                    if (money_json.getInteger("code") == 100 && money_json.containsKey("money")) {
                        JSONObject money = money_json.getJSONObject("money");
                        System.out.println("money:" + money);
                        if (money != null && money.containsKey("surplus")) {
                            if (cjson.getBigDecimal("number").compareTo(money.getBigDecimal("surplus")) != 1) {

                                int code = tijiao(cjson, map, uuid, fjson, 1);
                                json.put("code", code);
                                json.put("uuid", uuid);
                            } else {
                                json.put("code", 104);
                            }
                        } else {
                            json.put("code", 102);
                        }
                    } else {
                        json.put("code", 102);
                    }
                }
            } else {
                json.put("code", 102);
            }
        } else {
            json.put("code", 102);
        }
        return json;
    }


    public int tijiao(JSONObject cjson, Map<String, Object> map, String uuid, JSONObject fjson, int state) {
        int code = 100;
        try {
            if (state == 1) {
                JSONObject sell_json = fjson.getJSONObject("sell");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("currencyid", sell_json.getString("fb_currency_id"));
                jsonObject.put("userid", map.get("fuserid"));
                jsonObject.put("surplus", map.get("number"));
                fbMoneyInterface.FbMoneyUpdate(jsonObject);
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("number", cjson.getBigDecimal("number"));
            jsonObject.put("pid", cjson.getString("sellid"));
            jsonObject.put("style", "1");
            fbSellDao.FbSellUpdate(jsonObject);
            fbOrderDao.FbOrderInsert(map);
            map.put("orderid", uuid);
            map.put("state", 0);
            fbOrderDao.FbOrderLog(map);
            JSONObject m_user = fbUserInterface.FbUserSelect(map.get("userid").toString());
            JSONObject z_user = fbUserInterface.FbUserSelect(map.get("fuserid").toString());
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("pid", map.get("fuserid").toString());
            JSONObject p_json = fbPlayInterface.FbPlaySelect(jsonObject1);
            JSONObject json = new JSONObject();
            Map<String, Object> map1 = fbOrderDao.FbOrderDan(map);
            json.put("fjson", fjson);
            json.put("cjson", map1);
            json.put("muser", m_user);
            json.put("zuser", z_user);
            json.put("play", p_json);
            long datetime = new Date().getTime();
            long time = Long.parseLong(fjson.getString("time")) * 60;
            datetime = datetime + time * 1000;
            json.put("datetime", datetime);
            RedisUtils.time_set(uuid, json.toString(), 5, time);
            RedisUtils.hset("fb.order", datetime + "", 5, uuid);
            System.out.println("phone:" + z_user.getJSONObject("user").getString("phone"));
            System.out.println("phone:" + m_user.getJSONObject("user").getString("phone"));
            Sms.sendOrdereSms(z_user.getJSONObject("user").getString("phone"), map1.get("id").toString(), 0);
            Sms.sendOrdereSms(m_user.getJSONObject("user").getString("phone"), map1.get("id").toString(), 1);
        } catch (Exception e) {
            e.printStackTrace();
            code = 103;
        }
        return code;
    }


    @Override
    public JSONObject FbOrderSf(Map<String, Object> map) {
        JSONObject json = new JSONObject();
        try {
            Map<String, Object> fmap = fbOrderDao.FbOrderDan(map);
            if (fmap != null) {
                JSONObject jsonObject = new JSONObject(fmap);
                int state = 5;
                if (jsonObject.getInteger("state") == 0) {
                    Map<String, Object> cmap = new HashMap<>();
                    cmap.put("number", BigDecimal.ZERO.subtract(jsonObject.getBigDecimal("number")));
                    cmap.put("style", 0);
                    cmap.put("pid", jsonObject.get("sellid"));
                    fbSellDao.FbSellUpdate(cmap);
                    if (jsonObject.getInteger("type") == 1) {
                        Map<String, Object> cmap1 = new HashMap<>();
                        cmap1.put("currencyid", jsonObject.get("fb_currency_id"));
                        cmap1.put("userid", jsonObject.get("fuserid"));
                        cmap1.put("surplus", BigDecimal.ZERO.subtract(jsonObject.getBigDecimal("number")));
                        fbMoneyInterface.FbMoneyUpdate(cmap1);
                    }

                    state = 6;
                } else if (jsonObject.getInteger("state") == 1) {
                    state = 5;
                } else if (jsonObject.getInteger("state") == 2) {
                    state = 5;
                } else if (jsonObject.getInteger("state") == 3) {
                    state = 7;
                    Map<String, Object> score = fbScoreDao.FbScoreSelect();
                    if (score != null) {
                        Map<String, Object> maps = new HashMap<>();
                        maps.put("userid", jsonObject.get("userid"));
                        maps.put("puserid", jsonObject.get("fuserid"));
                        maps.put("type", jsonObject.get("type"));
                        maps.put("orderid", jsonObject.get("pid"));
                        maps.put("score", score.get("score"));
                        maps.put("reputation", score.get("score"));
                        fbEvaluateDao.FbEvakyateInsert(maps);
                        fbUserInterface.FbUserUpdateQ(maps);
                        maps.put("userid", jsonObject.get("fuserid"));
                        maps.put("puserid", jsonObject.get("userid"));
                        fbEvaluateDao.FbEvakyateInsert(maps);
                        fbUserInterface.FbUserUpdateQ(maps);
                    }
                } else if (jsonObject.getInteger("state") == 8) {
                    state = 7;
                    Map<String, Object> score = fbScoreDao.FbScoreSelect();
                    if (score != null) {
                        Map<String, Object> maps = new HashMap<>();
                        maps.put("userid", jsonObject.get("fuserid"));
                        maps.put("puserid", jsonObject.get("userid"));
                        maps.put("type", jsonObject.get("type"));
                        maps.put("orderid", jsonObject.get("pid"));
                        maps.put("score", score.get("score"));
                        maps.put("reputation", score.get("score"));
                        fbEvaluateDao.FbEvakyateInsert(maps);
                        fbUserInterface.FbUserUpdateQ(maps);
                    }
                } else if (jsonObject.getInteger("state") == 9) {
                    state = 7;
                    Map<String, Object> score = fbScoreDao.FbScoreSelect();
                    if (score != null) {
                        Map<String, Object> maps = new HashMap<>();
                        maps.put("userid", jsonObject.get("userid"));
                        maps.put("puserid", jsonObject.get("fuserid"));
                        maps.put("type", jsonObject.get("type"));
                        maps.put("orderid", jsonObject.get("pid"));
                        maps.put("score", score.get("score"));
                        maps.put("reputation", score.get("score"));
                        fbEvaluateDao.FbEvakyateInsert(maps);
                        fbUserInterface.FbUserUpdateQ(maps);
                    }
                }
                Map<String, Object> cmap = new HashMap<>();
                cmap.put("state", state);
                cmap.put("pid", jsonObject.get("pid"));
                fbOrderDao.FbOrderUpdate(cmap);
                if (state == 7) {
                    int count = fbOrderDao.FbOrderJx(jsonObject.getString("sellid"));
                    if (count == 0) {
                        Map<String, Object> smap = fbSellDao.FbSellDan(jsonObject.getString("sellid"));
                        if (smap != null && smap.containsKey("surplus") && smap.containsKey("min_money")) {
                            JSONObject sjson = new JSONObject(smap);
                            String cny = RedisUtils.get("CNY", 4);
                            String value = RedisUtils.get("kline." + sjson.get("currencyid"), 0);
                            if (value != null && value != "") {
                                JSONObject jsonObject1 = JSONObject.parseObject(value);
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
                }

                json.put("code", 100);
            } else {
                json.put("code", 102);
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.put("code", 103);
        }
        return json;
    }


    @Override
    public JSONObject FbOrderSelect(Map<String, Object> map) {
        JSONObject json = new JSONObject();
        try {
            String value = RedisUtils.get(map.get("pid").toString(), 5);
            if (value == null || value == "") {
                Map<String, Object> maps = fbOrderDao.FbOrderDan(map);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("cjson", maps);
                JSONObject fjson = fbSellInterface.FbSellDan(maps.get("sellid").toString(), maps.get("fuserid").toString());
                jsonObject.put("fjson", fjson);
                if (map.get("userid").toString().equals(maps.get("userid")) || map.get("userid").toString().equals(maps.get("fuserid"))) {
                    JSONObject m_user = fbUserInterface.FbUserSelect(maps.get("userid").toString());
                    JSONObject z_user = fbUserInterface.FbUserSelect(maps.get("fuserid").toString());
                    jsonObject.put("muser", m_user);
                    jsonObject.put("zuser", z_user);
                    json.put("state", 1);
                    json.put("message", jsonObject);
                    json.put("code", 100);
                    if (map.get("userid").toString().equals(maps.get("userid"))) {
                        json.put("type", 1);
                    } else if (map.get("userid").toString().equals(maps.get("fuserid"))) {
                        json.put("type", 0);
                    }
                } else {
                    json.put("code", 102);
                }
            } else {
                JSONObject fjon = JSONObject.parseObject(value);

                if (fjon.containsKey("cjson")) {
                    JSONObject cjson = fjon.getJSONObject("cjson");
                    if (map.get("userid").toString().equals(cjson.get("userid")) || map.get("userid").toString().equals(cjson.get("fuserid"))) {
                        json.put("message", fjon);
                        json.put("state", 0);
                        json.put("code", 100);
                        json.put("datetime", new Date().getTime());

                        if (map.get("userid").toString().equals(cjson.get("userid"))) {
                            json.put("type", 1);
                        } else if (map.get("userid").toString().equals(cjson.get("fuserid"))) {
                            json.put("type", 0);
                        }
                    } else {
                        json.put("code", 102);
                    }
                } else {
                    json.put("code", 102);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            json.put("code", 103);
        }
        return json;
    }


    @Override
    public JSONObject FbOrderUpdate(Map<String, Object> map) {
        JSONObject json = new JSONObject();
        try {
            String value = RedisUtils.get(map.get("pid").toString(), 5);
            if (value == null || value == "") {
                json.put("code", 102);
            } else {
                JSONObject fjson = JSONObject.parseObject(value);
                JSONObject cjson = fjson.getJSONObject("cjson");
                JSONObject ffjson = fjson.getJSONObject("fjson");
                JSONObject sell = fjson.getJSONObject("fjson").getJSONObject("sell");
                if ("0".equals(map.get("type"))) {
                    System.out.println(cjson.getString("fuserid") + "===" + map.get("userid"));
                    if (cjson.getString("fuserid").equals(map.get("userid"))) {
                        boolean state = true;
                        if ("3".equals(map.get("state"))) {
                            String pasword = map.get("password").toString();
                            map.put("password", MD5Util.MD5(pasword));
                            map.put("sellid", cjson.get("sellid"));
                            int num = subUserDao.SubUserCount(map);
                            if (num != 0) {
                                if (cjson.getInteger("type") == 0) {
                                    System.out.println("---1-----");
                                    Map<String, Object> maps = new HashMap<>();
                                    maps.put("userid", cjson.get("userid"));
                                    maps.put("type", "1");
                                    maps.put("currencyid", sell.get("fb_currency_id"));
                                    maps.put("surplus", BigDecimal.ZERO.subtract(cjson.getBigDecimal("number")));
                                    fbMoneyInterface.FbMoneyUpdate(maps);
                                    maps.remove("type");
                                    maps.put("type", "1");
                                    maps.put("userid", cjson.get("fuserid"));
                                    BigDecimal sxf = cjson.getBigDecimal("number").multiply(sell.getBigDecimal("poundage")).divide(new BigDecimal(100), 8, BigDecimal.ROUND_HALF_UP);
                                    maps.put("surplus", BigDecimal.ZERO.subtract(cjson.getBigDecimal("number").add(sxf)));

                                    Map<String, Object> user_map = new HashMap<>();
                                    user_map.put("userid", cjson.get("fuserid"));
                                    user_map.put("trad_volume", "1");
                                    fbUserInterface.FbUserUpdateQ(user_map);

                                    Map<String, Object> pmap = new HashMap<>();
                                    pmap.put("poundage", sxf);
                                    pmap.put("sellid", map.get("pid"));
                                    pmap.put("userid", cjson.get("fuserid"));
                                    pmap.put("currencyid", sell.get("fb_currency_id"));
                                    fbPoundageLogDao.FbPoundageLogInsert(pmap);
                                } else {
                                    System.out.println("---2-----");
                                    Map<String, Object> maps = new HashMap<>();
                                    maps.put("userid", cjson.get("userid"));
                                    maps.put("type", "1");
                                    maps.put("currencyid", sell.get("fb_currency_id"));
                                    BigDecimal sxf = cjson.getBigDecimal("number").multiply(sell.getBigDecimal("poundage")).divide(new BigDecimal(100), 8, BigDecimal.ROUND_HALF_UP);
                                    maps.put("surplus", BigDecimal.ZERO.subtract(cjson.getBigDecimal("number").subtract(sxf)));
                                    fbMoneyInterface.FbMoneyUpdate(maps);
                                    maps.remove("type");
                                    maps.put("state", "1");
                                    maps.put("userid", cjson.get("fuserid"));
                                    fbMoneyInterface.FbMoneyUpdate(maps);
                                    Map<String, Object> user_map = new HashMap<>();
                                    user_map.put("userid", cjson.get("userid"));
                                    user_map.put("trad_volume", "1");
                                    fbUserInterface.FbUserUpdateQ(user_map);
                                    Map<String, Object> pmap = new HashMap<>();
                                    pmap.put("poundage", sxf);
                                    pmap.put("sellid", map.get("pid"));
                                    pmap.put("userid", cjson.get("userid"));
                                    pmap.put("currencyid", sell.get("fb_currency_id"));
                                    fbPoundageLogDao.FbPoundageLogInsert(pmap);

                                }
                                state = true;
                            } else {
                                state = false;
                                json.put("code", 105);
                            }

                        }
                        if (state == true) {
                            fbOrderDao.FbOrderUpdate(map);
                            map.put("orderid", map.get("pid"));
                            map.put("type", cjson.get("type"));
                            fbOrderDao.FbOrderLog(map);
                            cjson.put("state", map.get("state"));
                            fjson.put("cjson", cjson);
                            long datetime = new Date().getTime();
                            long time = Long.parseLong(ffjson.getString("time")) * 60;
                            datetime = datetime + time * 1000;
                            fjson.put("datetime", datetime);
                            System.out.println("----------------------");
                            RedisUtils.del(map.get("pid").toString(), 5);
                            RedisUtils.time_set(map.get("pid").toString(), fjson.toString(), 5, time);
                            json.put("code", 100);
                        }

                    } else {
                        json.put("code", 104);
                    }
                } else {
                    System.out.println(cjson.getString("userid") + "==2=" + map.get("userid"));
                    if (cjson.getString("userid").equals(map.get("userid"))) {
                        fbOrderDao.FbOrderUpdate(map);
                        map.put("orderid", map.get("pid"));
                        map.put("type", cjson.get("type"));
                        fbOrderDao.FbOrderLog(map);
                        long datetime = new Date().getTime();
                        long time = Long.parseLong(ffjson.getString("time")) * 60;
                        datetime = datetime + time * 1000;
                        cjson.put("state", map.get("state"));
                        fjson.put("cjson", cjson);
                        fjson.put("datetime", datetime);
                        System.out.println("----------2------------");
                        RedisUtils.del(map.get("pid").toString(), 5);
                        RedisUtils.time_set(map.get("pid").toString(), fjson.toString(), 5, time);
                        json.put("code", 100);
                    } else {
                        json.put("code", 104);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.put("code", 103);
        }
        return json;
    }


    @Override
    public JSONObject FbOrderSelectQ(String userid, int state, int page, int num) {
        JSONObject json = new JSONObject();
        try {
            List<Map<String, Object>> list = fbOrderDao.FbOrderSelect(userid, state, page, num);
            int count = fbOrderDao.FbOrderCount(userid, state);
            json.put("order", list);
            json.put("total", count);
            json.put("code", 100);
            json.put("userid", userid);
        } catch (Exception e) {
            e.printStackTrace();
            json.put("code", 103);
        }
        return json;
    }
}

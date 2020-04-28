package com.whp.usdtfb.user.Impl;

import com.alibaba.fastjson.JSONObject;
import com.whp.usdtfb.Util.utils.GoogleAuthenticator;
import com.whp.usdtfb.Util.utils.MD5Util;
import com.whp.usdtfb.Util.utils.RedisUtils;
import com.whp.usdtfb.user.Dao.FbFriendDao;
import com.whp.usdtfb.user.Dao.FbPoundageLogDao;
import com.whp.usdtfb.user.Dao.FbSellDao;
import com.whp.usdtfb.user.Dao.SubUserDao;
import com.whp.usdtfb.user.Interface.CnyInterface;
import com.whp.usdtfb.user.Interface.FbMoneyInterface;
import com.whp.usdtfb.user.Interface.FbSellInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author : 张吉伟
 * @data : 2018/12/26 15:03
 * @descrpition :
 */
@Service
public class FbSellImpl implements FbSellInterface {
    @Autowired
    private FbSellDao fbSellDao;
    @Autowired
    private FbMoneyInterface fbMoneyInterface;
    @Autowired
    private CnyInterface cnyInterface;
    @Autowired
    private FbFriendDao fbFriendDao;
    @Autowired
    private SubUserDao subUserDao;

    @Autowired
    private FbPoundageLogDao fbPoundageLogDao;

    @Override
    public JSONObject FbSellInsert(Map<String, Object> map) {
        JSONObject json = new JSONObject();
        try {
            if (map.containsKey("currencyid") && map.containsKey("userid") && map.containsKey("type") && map.containsKey("number") && map.containsKey("password")) {
                JSONObject jjson = new JSONObject(map);
                String password = MD5Util.MD5(map.get("password").toString());
                map.put("password", password);
                int num = subUserDao.SubUserCount(map);
                if (num == 1) {
                    if (map.containsKey("code")) {
                        Map<String, Object> google = subUserDao.SubUserGoogle(map);
                        if (google == null) {
                            json.put("code", 103);
                            return json;
                        } else {
                            if ("1".equals(google.get("isgoogle"))) {
                                GoogleAuthenticator go = new GoogleAuthenticator();
                                long time = System.currentTimeMillis();
                                boolean z = go.check_code(google.get("google_secret").toString(), Long.parseLong(map.get("code").toString()), time);
                                if (z == false) {
                                    json.put("code", 107);
                                    return json;
                                }
                            } else {
                                json.put("code", 106);
                                return json;
                            }
                        }
                    }
                    BigDecimal bigDecimal = BigDecimal.ZERO;

                    JSONObject fjson = fbMoneyInterface.FbMoneySelect(map.get("currencyid").toString(), map.get("userid").toString(), "1");
                    if (fjson.getInteger("code") == 100) {
                        if (fjson.containsKey("money")) {
                            JSONObject mjson = fjson.getJSONObject("money");
                            if (mjson.containsKey("surplus")) {
                                bigDecimal = mjson.getBigDecimal("surplus");
                                if ("0".equals(map.get("type"))) {
                                    if (bigDecimal.compareTo(jjson.getBigDecimal("number").add(jjson.getBigDecimal("number").multiply(jjson.getBigDecimal("sxf")).divide(BigDecimal.valueOf(100), 4, BigDecimal.ROUND_HALF_UP))) == -1) {
                                        json.put("code", 104);
                                        return json;
                                    }
                                }
                            } else {
                                json.put("code", 103);
                                return json;
                            }
                        } else {
                            json.put("code", 102);
                            return json;
                        }
                    } else {
                        json.put("code", 103);
                        return json;
                    }
                    String uuid = UUID.randomUUID().toString();
                    map.put("pid", uuid);
                    map.put("poundage", jjson.getBigDecimal("sxf"));
                    fbSellDao.FbSellInsert(map);

                    JSONObject cjson = new JSONObject();
                    if ("0".equals(map.get("type"))) {
                        cjson.put("surplus", jjson.getBigDecimal("number").add(jjson.getBigDecimal("number").multiply(jjson.getBigDecimal("sxf")).divide(BigDecimal.valueOf(100), 4, BigDecimal.ROUND_HALF_UP)));
                        cjson.put("currencyid", jjson.getString("currencyid"));
                        cjson.put("userid", jjson.getString("userid"));
                        fbMoneyInterface.FbMoneyUpdate(cjson);
                    }

//                    cjson.put("poundage", jjson.getBigDecimal("number").multiply(jjson.getBigDecimal("sxf")).divide(BigDecimal.valueOf(100), 4, BigDecimal.ROUND_HALF_UP));
//                    cjson.put("sellid", uuid);
//                    fbPoundageLogDao.FbPoundageLogInsert(cjson);

                    json.put("code", 100);
                } else {
                    json.put("code", 105);
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


    @Override
    public JSONObject FbSellSelect(Map<String, Object> map) {
        JSONObject json = new JSONObject();
        try {
            List<Map<String, Object>> list = fbSellDao.FbSellSelect(map, Integer.parseInt(map.get("page").toString()), Integer.parseInt(map.get("num").toString()));
            int count = fbSellDao.FbSellCount(map);
            json.put("sell", list);
            json.put("total", count);
            json.put("code", 100);
        } catch (Exception e) {
            e.printStackTrace();
            json.put("code", 103);
        }

        return json;
    }

    @Override
    public JSONObject FbSellUpdateState(Map<String, Object> map) {
        JSONObject json = new JSONObject();
        try {
            fbSellDao.FbSellStateUpdate(map);
            json.put("code", 100);
        } catch (Exception e) {
            e.printStackTrace();
            json.put("code", 103);
        }
        return json;
    }

    @Override
    public JSONObject FbSellUpdate(Map<String, Object> map) {
        JSONObject json = new JSONObject();
        try {
            String password = MD5Util.MD5(map.get("password").toString());
            map.put("password", password);
            int num = subUserDao.SubUserCount(map);
            if (num == 1) {
                if (map.containsKey("code")) {
                    Map<String, Object> google = subUserDao.SubUserGoogle(map);
                    if (google == null) {
                        json.put("code", 103);
                        return json;
                    } else {
                        if ("1".equals(google.get("isgoogle"))) {
                            GoogleAuthenticator go = new GoogleAuthenticator();
                            long time = System.currentTimeMillis();
                            boolean z = go.check_code(google.get("google_secret").toString(), Long.parseLong(map.get("code").toString()), time);
                            if (z == false) {
                                json.put("code", 107);
                                return json;
                            }
                        } else {
                            json.put("code", 106);
                            return json;
                        }
                    }
                }
                Map<String, Object> fmap = fbSellDao.FbSellDan(map.get("pid").toString());
                if ("0".equals(fmap.get("style"))) {
                    fbSellDao.FbSellDanUpdate(map);
                    json.put("code", 100);
                } else {
                    json.put("code", 104);
                }
            } else {
                json.put("code", 105);
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.put("code", 103);
        }
        return json;
    }

    @Override
    public JSONObject FbSellDan(String pid, String userid) {
        JSONObject json = new JSONObject();
        try {
            Map<String, Object> map = fbSellDao.FbSellDan(pid);
            if (map != null && map.containsKey("currencyid")) {
                JSONObject jsonObject = cnyInterface.CNY(map.get("currencyid").toString());
                json.put("cny", jsonObject);
                JSONObject jsonObject1 = fbMoneyInterface.FbMoneySelect(map.get("fb_currency_id").toString(), userid, null);
                json.put("money", jsonObject1);
                String time = RedisUtils.get("limit.time", 4);
                json.put("time", time);
                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.put("userid", userid);
                jsonObject2.put("yuserid", map.get("userid"));
                int count = fbFriendDao.FbFriendCount(jsonObject2);
                json.put("friend", count);

            }
            json.put("code", 100);
            json.put("sell", map);
        } catch (Exception e) {
            e.printStackTrace();
            json.put("code", 103);
        }
        return json;
    }
}

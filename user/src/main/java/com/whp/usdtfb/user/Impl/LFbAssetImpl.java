package com.whp.usdtfb.user.Impl;

import com.alibaba.fastjson.JSONObject;
import com.whp.usdtfb.Util.utils.GoogleAuthenticator;
import com.whp.usdtfb.Util.utils.MD5Util;
import com.whp.usdtfb.Util.utils.RedisUtils;
import com.whp.usdtfb.user.Dao.*;
import com.whp.usdtfb.user.Interface.LFbAssetInterface;
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
public class LFbAssetImpl implements LFbAssetInterface {

    @Autowired
    private LFbAssetDao lfbAssetDao;
    @Autowired
    private FbUserDao fbUserDao;
    @Autowired
    private SubUserDao subUserDao;
    @Autowired
    private LFbSysWalletDao lFbSysWalletDao;
    @Autowired
    private FbMoneyDao fbMoneyDao;

    @Override
    public JSONObject LFbAssetSelect(String userid) {
        JSONObject json = new JSONObject();
        try {
            // 法币情报
            List<Map<String, Object>> list = lfbAssetDao.LFbAssetSelect(userid);

            // 现货情报
            List<Map<String, Object>> usdtList = lfbAssetDao.LFbAssetUsdtSelect(userid);

            json.put("asset", list);
            json.put("usdtAsset", usdtList);
            json.put("code", 100);
        } catch (Exception e) {
            e.printStackTrace();
            json.put("code", 103);
        }
        return json;
    }

    @Override
    public JSONObject LFbAssetInsert(Map<String, Object> map) {
        JSONObject json = new JSONObject();
        try {
            String code = RedisUtils.get("sms." + map.get("userid").toString(), 8);
            if (code != null && code != "" && code.equals(map.get("yzm"))) {
                Map<String, Object> fmap = fbUserDao.FbUserSelect(map.get("userid").toString());
                if ("1".equals(fmap.get("isgoogle")) && fmap.containsKey("google_secret")) {
                    GoogleAuthenticator go = new GoogleAuthenticator();
                    long time = System.currentTimeMillis();
                    boolean z = go.check_code(fmap.get("google_secret").toString(), Long.parseLong(map.get("google_code").toString()), time);
                    if (z == false) {
                        json.put("code", 105);
                        return json;
                    } else {
                        String password = MD5Util.MD5(map.get("password").toString());
                        map.put("password", password);
                        int num = subUserDao.SubUserCount(map);
                        if (num > 0) {
                            Map<String, Object> zmap = lFbSysWalletDao.FbSysWalletSelect(Integer.parseInt(map.get("codeid").toString()));
                            map.put("from_wallet", zmap.get("address"));
                            Map<String, Object> smap = fbMoneyDao.FbMoneySelect(map.get("currencyid").toString(), map.get("userid").toString(), null);
                            if (new BigDecimal(map.get("money").toString()).add(new BigDecimal(map.get("trade").toString())).compareTo(new BigDecimal(smap.get("surplus").toString())) != 1) {
                                JSONObject cjson = new JSONObject();
                                cjson.put("currencyid", map.get("currencyid"));
                                cjson.put("userid", map.get("userid"));
                                cjson.put("surplus", new BigDecimal(map.get("money").toString()).add(new BigDecimal(map.get("trade").toString())));
                                fbMoneyDao.FbMoneyUpdate(cjson);
                                lfbAssetDao.LFbAssetUsdtInsert(map);


                                json.put("code", 100);
                            } else {
                                json.put("code", 107);
                            }

                        } else {
                            json.put("code", 106);
                        }
                    }
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

package com.whp.usdtfb.user.Impl;

import com.alibaba.fastjson.JSONObject;
import com.whp.usdtfb.Util.utils.RedisUtils;
import com.whp.usdtfb.user.Dao.FbCurrencyDao;
import com.whp.usdtfb.user.Dao.FbPoundageLogDao;
import com.whp.usdtfb.user.Dao.LFbAssetOrderDetailDao;
import com.whp.usdtfb.user.Interface.CnyInterface;
import com.whp.usdtfb.user.Interface.LFbAssetOrderDetailInterface;
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
public class LFbAssetOrderDetailImpl implements LFbAssetOrderDetailInterface {

    @Autowired
    private LFbAssetOrderDetailDao lFbAssetOrderDetailDao;
    @Autowired
    private FbPoundageLogDao fbPoundageLogDao;
    @Autowired
    private CnyInterface cnyInterface;
    @Autowired
    private FbCurrencyDao fbCurrencyDao;

    @Override
    public JSONObject LFbAssetOrderDetailSelect(Map<String, Object> map, String userid) {
        JSONObject json = new JSONObject();

        try {
            // 买入情报取得
            List<Map<String, Object>> gmList = lFbAssetOrderDetailDao.LFbAssetOrderDetailSelect(map, userid, "0");

            // 售出情报取得
            List<Map<String, Object>> scList = lFbAssetOrderDetailDao.LFbAssetOrderDetailSelect(map, userid, "1");

            // 划转情报取得
            List<Map<String, Object>> transferList = lFbAssetOrderDetailDao.LFbTransferDetailSelect(map, userid);
            // 手续费取得
            String currencyid = "";
            if (map.containsKey("currencyid")) {
                currencyid = map.get("currencyid").toString();
            }
            List<Map<String, Object>> poundageList = fbPoundageLogDao.FbPoundageSelect(currencyid, userid);
//            Map<String,Object> fmap=fbCurrencyDao.FbCurrencyUsdt(currencyid);
//           JSONObject cny =cnyInterface.CNY(fmap.get("currencyid").toString());
            json.put("gmList", gmList);
            json.put("scList", scList);
            json.put("transferList", transferList);
            json.put("poundageList", poundageList);
            //  json.put("cny", cny);
            json.put("code", 100);

        } catch (Exception e) {
            e.printStackTrace();
            json.put("code", 103);
        }
        return json;
    }
}

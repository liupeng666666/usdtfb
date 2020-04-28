package com.whp.usdtfb.user.Impl;

import com.alibaba.fastjson.JSONObject;
import com.whp.usdtfb.user.Dao.FbCurrencyDao;
import com.whp.usdtfb.user.Dao.FbPoundageDao;
import com.whp.usdtfb.user.Interface.CnyInterface;
import com.whp.usdtfb.user.Interface.FbCurrencyInterface;
import com.whp.usdtfb.user.Interface.FbMoneyInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2018/12/22 22:43
 * @descrpition :
 */
@Service
public class FbCurrencyImpl implements FbCurrencyInterface {
    @Autowired
    private FbCurrencyDao fbCurrencyDao;
    @Autowired
    private CnyInterface cnyInterface;
    @Autowired
    private FbMoneyInterface fbMoneyInterface;
    @Autowired
    private FbPoundageDao fbPoundageDao;

    @Override
    public JSONObject FbCurrencyDan(String pid, String currencyid, String fbcurrencyid) {
        JSONObject json = new JSONObject();
        try {
            JSONObject jsonObject = cnyInterface.CNY(currencyid);
            json.put("cny", jsonObject);
            JSONObject jsonObject1 = fbMoneyInterface.FbMoneySelect(fbcurrencyid, pid, null);
            json.put("money", jsonObject1);
            List<Map<String, Object>> flist = fbPoundageDao.FbPoundageSelect(fbcurrencyid);
            json.put("poundage", flist);
            json.put("code", 100);
        } catch (Exception e) {
            e.printStackTrace();
            json.put("code", 103);
        }
        return json;
    }

    @Override
    public JSONObject FbCurrencySelect(String userid) {
        JSONObject json = new JSONObject();
        try {
            List<Map<String, Object>> list = fbCurrencyDao.FbCurrencySelect();
            if (list.size() > 0) {
                JSONObject jsonObject = cnyInterface.CNY(list.get(0).get("currencyid").toString());
                json.put("cny", jsonObject);
                JSONObject jsonObject1 = fbMoneyInterface.FbMoneySelect(list.get(0).get("pid").toString(), userid, null);
                json.put("money", jsonObject1);
                List<Map<String, Object>> flist = fbPoundageDao.FbPoundageSelect(list.get(0).get("pid").toString());
                json.put("poundage", flist);

            }

            json.put("currency", list);
            json.put("code", 100);
        } catch (Exception e) {
            e.printStackTrace();
            json.put("code", 103);
        }
        return json;
    }
}

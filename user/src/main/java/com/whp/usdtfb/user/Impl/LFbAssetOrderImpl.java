package com.whp.usdtfb.user.Impl;

import com.alibaba.fastjson.JSONObject;
import com.whp.usdtfb.Util.utils.RedisUtils;
import com.whp.usdtfb.user.Dao.LFbAssetOrderDao;
import com.whp.usdtfb.user.Interface.LFbAssetOrderInterface;
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
public class LFbAssetOrderImpl implements LFbAssetOrderInterface {

    @Autowired
    private LFbAssetOrderDao lfbAssetOrderDao;

    @Override
    public JSONObject LFbAssetOrderSelect(String userid) {
        JSONObject json = new JSONObject();
        try {
            BigDecimal profitSum = BigDecimal.ZERO;
            BigDecimal profitSumCny = BigDecimal.ZERO;
            List<Map<String, Object>> list = lfbAssetOrderDao.LFbAssetOrderSelect(userid);

            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {

                    JSONObject fjson = new JSONObject(list.get(i));

                    JSONObject jsonMoney = JSONObject.parseObject(RedisUtils.get("kline." + fjson.getString("currencyid"), 0));
                    BigDecimal money = jsonMoney.getBigDecimal("close");

                    fjson.put("money", money);
                    list.set(i, fjson);

                    profitSum = profitSum.add(fjson.getBigDecimal("moneySum").multiply(money));
                }

                BigDecimal cny = new BigDecimal(RedisUtils.get("CNY", 4));
                profitSumCny = profitSum.multiply(cny);
            }

            json.put("asset", list);
            json.put("profitSum", profitSum.setScale(2, BigDecimal.ROUND_HALF_UP));
            json.put("profitSumCny", profitSumCny.setScale(2, BigDecimal.ROUND_HALF_UP));
            json.put("code", 100);
        } catch (Exception e) {
            e.printStackTrace();
            json.put("code", 103);
        }
        return json;
    }

    @Override
    public JSONObject LFbAssetOrderUsdtSelect(String userid) {
        JSONObject json = new JSONObject();
        JSONObject jsonMoney = null;
        BigDecimal money = BigDecimal.ZERO;

        try {
            Map<String, Object> usdtInfo = lfbAssetOrderDao.LFbAssetOrderUsdtSelect(userid);

            if (usdtInfo != null) {
                jsonMoney = JSONObject.parseObject(RedisUtils.get("kline.usdt", 0));
                money = jsonMoney.getBigDecimal("close");
                usdtInfo.put("moneyUsdt", money);

                jsonMoney = JSONObject.parseObject(RedisUtils.get("kline.bru", 0));
                money = jsonMoney.getBigDecimal("close");
                usdtInfo.put("moneyBru", money);
            }

            json.put("assetUsdt", usdtInfo);
            String cny = RedisUtils.get("CNY", 4);
            json.put("cny", cny);
            json.put("code", 100);
        } catch (Exception e) {
            e.printStackTrace();
            json.put("code", 103);
        }
        return json;
    }

    @Override
    public JSONObject LFbAssetOrderUpdate(Map<String, Object> map, String userid) {

        JSONObject json = new JSONObject();
        BigDecimal surplusUpd = BigDecimal.ZERO;
        BigDecimal surplusAdd = BigDecimal.ZERO;
        String from_currency = "";
        String to_currency = "";
        String type = "";

        try {

            // 划转单价取得
            JSONObject jsonFrmMoney = JSONObject.parseObject(RedisUtils.get("kline." + map.get("moneyKeyFrm").toString(), 0));
            BigDecimal frmMoney = jsonFrmMoney.getBigDecimal("close");

            // 转至单价取得
            JSONObject jsonToMoney = JSONObject.parseObject(RedisUtils.get("kline." + map.get("moneyKeyTo").toString(), 0));
            BigDecimal toMoney = jsonToMoney.getBigDecimal("close");

            // 货币转换
            surplusUpd = new BigDecimal(map.get("surplusUpd").toString());
            surplusAdd = (surplusUpd.multiply(frmMoney)).divide(toMoney, 8, BigDecimal.ROUND_HALF_UP);
            map.put("surplusAdd", surplusAdd);

            // 划转表的信息判断
            if (map.get("currencyFrmId") != null && map.get("currencyFrmId") != "") {

                from_currency = map.get("currencyFrmId").toString();

                if (map.get("currencyToId") != null && map.get("currencyToId") != "") {
                    to_currency = map.get("currencyToId").toString();
                    type = "3";
                } else {

                    // USDT的场合
                    if ("USDT".equals(map.get("currencyToName").toString())) {
                        to_currency = "USDT";
                    } else {
                        // BRU的场合
                        to_currency = "BRU";
                    }
                    type = "1";
                }
            } else {

                // USDT的场合
                if ("USDT".equals(map.get("currencyFrmName").toString())) {
                    from_currency = "USDT";
                } else {
                    // BRU的场合
                    from_currency = "BRU";
                }

                if (map.get("currencyToId") != null && map.get("currencyToId") != "") {
                    to_currency = map.get("currencyToId").toString();
                    type = "0";
                } else {
                    // USDT的场合
                    if ("USDT".equals(map.get("currencyToName").toString())) {
                        to_currency = "USDT";
                    } else {
                        // BRU的场合
                        to_currency = "BRU";
                    }
                    type = "2";
                }
            }

            if (map.get("currencyFrmId") != null && map.get("currencyFrmId") != "") {
                lfbAssetOrderDao.LFbAssetOrderUpd1(map, userid);
            } else {
                lfbAssetOrderDao.LFbAssetOrderUpd3(map, userid);
            }

            if (map.get("currencyToId") != null && map.get("currencyToId") != "") {

                int orderCnt = lfbAssetOrderDao.LFbAssetOrderCnt(map.get("currencyToId").toString(), userid);

                if (orderCnt == 0) {
                    lfbAssetOrderDao.LFbAssetOrderIns(map.get("currencyToName").toString(),
                            surplusAdd, map.get("currencyToId").toString(), userid);
                } else {
                    lfbAssetOrderDao.LFbAssetOrderUpd2(map, userid);
                }

            } else {
                lfbAssetOrderDao.LFbAssetOrderUpd4(map, userid);
            }

            // 划转表登录
            lfbAssetOrderDao.LFbAssetTransferIns(from_currency, to_currency, surplusUpd, surplusAdd, userid, type);

            json.put("code", 100);
        } catch (Exception e) {
            e.printStackTrace();
            json.put("code", 103);
        }
        return json;
    }
}

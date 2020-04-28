package com.whp.usdtfb.user.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.whp.usdtfb.Util.utils.RedisUtils;
import com.whp.usdtfb.user.Interface.CnyInterface;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author : 张吉伟
 * @data : 2018/12/23 10:29
 * @descrpition :
 */
@Service
public class CnyImpl implements CnyInterface {
    @Override
    public JSONObject CNY(String pid) {
        JSONObject jsonObject = JSONObject.parseObject(RedisUtils.get("kline." + pid, 0));
        BigDecimal money = jsonObject.getBigDecimal("close");
        BigDecimal cny = BigDecimal.valueOf(Double.parseDouble(RedisUtils.get("CNY", 4)));
        BigDecimal cny_money = money.multiply(cny);
        JSONObject json = new JSONObject();
        json.put("cny", cny_money.setScale(2, BigDecimal.ROUND_HALF_UP));
        json.put("money", money);
        return json;
    }
}

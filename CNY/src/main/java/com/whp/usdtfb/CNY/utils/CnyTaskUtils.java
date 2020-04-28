package com.whp.usdtfb.CNY.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.whp.usdtfb.CNY.Interface.FbCNYInterface;
import com.whp.usdtfb.Util.utils.HttpClientUtil;
import com.whp.usdtfb.Util.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author : 张吉伟
 * @data : 2018/12/18 21:37
 * @descrpition :
 */
@Component
public class CnyTaskUtils {

    @Autowired
    private FbCNYInterface fbCNYInterface;

    @Scheduled(initialDelay = 1000, fixedRate = 900000)
    public void task() {
        String value = HttpClientUtil.doGet("http://op.juhe.cn/onebox/exchange/query");
        JSONObject json = JSONObject.parseObject(value);
        if (json.getInteger("error_code") == 0) {
            if (json.containsKey("result")) {
                JSONObject json1 = json.getJSONObject("result");
                if (json1.containsKey("list")) {
                    JSONArray array = json1.getJSONArray("list");
                    for (Object o : array) {
                        JSONArray array1 = JSONArray.parseArray(o.toString());
                        if (array1.contains("美元")) {
                            BigDecimal bigDecimal = BigDecimal.valueOf(Double.parseDouble(array1.getString(5)));
                            RedisUtils.set("CNY", bigDecimal.divide(BigDecimal.valueOf(100), 4, BigDecimal.ROUND_HALF_UP).toString(), 4);
                            fbCNYInterface.FbCNYInsert(bigDecimal.divide(BigDecimal.valueOf(100), 4, BigDecimal.ROUND_HALF_UP));
                            break;
                        }
                    }
                }
            }
        }
    }

}

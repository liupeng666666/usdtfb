package com.whp.usdtfb.user.Controller;

import com.alibaba.fastjson.JSONObject;
import com.whp.usdtfb.Util.utils.HttpClientUtils;
import com.whp.usdtfb.Util.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2019/1/28 22:34
 * @descrpition :
 */
@RestController
@RequestMapping("/FbWallet")
public class FbWalletController {
    @Value("${omni.url}")
    private String url;

    @PostMapping("Address")
    public JSONObject FbWallet(@RequestParam("codeid") int codeid, HttpServletRequest request) {
        String pid = JWTUtil.getUsername(request.getHeader("Authorization"), "pid");
        Map<String, String> map = new HashMap<>();
        map.put("userid", pid);
        String value = null;
        if (codeid == 1) {

            value = HttpClientUtils.doPost(url + "/omni/eth", "UTF-8", map);

        } else if (codeid == 2) {
            value = HttpClientUtils.doPost(url + "/omni/etc", "UTF-8", map);
        } else if (codeid == 3) {
            value = HttpClientUtils.doPost(url + "/omni/btc", "UTF-8", map);
        } else if (codeid == 4) {
            value = HttpClientUtils.doPost(url + "/omni/bch", "UTF-8", map);
        } else if (codeid == 5) {
            value = HttpClientUtils.doPost(url + "/omni/ltc", "UTF-8", map);
        }
        JSONObject jsonObject = JSONObject.parseObject(value);
        return jsonObject;
    }
}

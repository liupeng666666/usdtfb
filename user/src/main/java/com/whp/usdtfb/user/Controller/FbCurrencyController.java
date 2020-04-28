package com.whp.usdtfb.user.Controller;

import com.alibaba.fastjson.JSONObject;
import com.whp.usdtfb.Util.utils.JWTUtil;
import com.whp.usdtfb.user.Interface.FbCurrencyInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : 张吉伟
 * @data : 2018/12/22 22:45
 * @descrpition :
 */
@RestController
@RequestMapping("/FbCurrency")
public class FbCurrencyController {
    @Autowired
    private FbCurrencyInterface fbCurrencyInterface;

    @PostMapping("FbCurrencySelect")
    public JSONObject FbCurrencySelect(HttpServletRequest request) {
        String pid = JWTUtil.getUsername(request.getHeader("Authorization"), "pid");
        JSONObject json = fbCurrencyInterface.FbCurrencySelect(pid);
        return json;
    }

    @PostMapping("FbCurrencyDan")
    public JSONObject FbCurrencyDan(@RequestParam String currencyid, @RequestParam String fbcurrencyid, HttpServletRequest request) {
        String pid = JWTUtil.getUsername(request.getHeader("Authorization"), "pid");
        JSONObject json = fbCurrencyInterface.FbCurrencyDan(pid, currencyid, fbcurrencyid);
        return json;
    }
}

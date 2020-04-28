package com.whp.usdtfb.user.Controller;

import com.alibaba.fastjson.JSONObject;
import com.whp.usdtfb.Util.utils.JWTUtil;
import com.whp.usdtfb.user.Interface.LFbAssetOrderInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author : 张吉玲
 * @data : 2019/01/02 22:37
 * @descrpition :
 */
@RestController
@RequestMapping("LFbAssetOrder")
public class LFbAssetOrderController {

    @Autowired
    private LFbAssetOrderInterface lFbAssetOrderInterface;

    @PostMapping("LFbAssetOrderSelect")
    public JSONObject LFbAssetOrderSelect(HttpServletRequest request) {
        String pid = JWTUtil.getUsername(request.getHeader("Authorization"), "pid");
        JSONObject json = lFbAssetOrderInterface.LFbAssetOrderSelect(pid);
        return json;
    }

    @PostMapping("LFbAssetOrderUsdtSelect")
    public JSONObject LFbAssetOrderUsdtSelect(HttpServletRequest request) {
        String pid = JWTUtil.getUsername(request.getHeader("Authorization"), "pid");
        JSONObject json = lFbAssetOrderInterface.LFbAssetOrderUsdtSelect(pid);
        return json;
    }

    @PostMapping("LFbAssetOrderUpdate")
    public JSONObject LFbAssetOrderUpdate(@RequestParam Map<String, Object> map, HttpServletRequest request) {
        String pid = JWTUtil.getUsername(request.getHeader("Authorization"), "pid");
        JSONObject json = lFbAssetOrderInterface.LFbAssetOrderUpdate(map, pid);
        return json;
    }

}

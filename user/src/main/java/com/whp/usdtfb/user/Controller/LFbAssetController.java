package com.whp.usdtfb.user.Controller;

import com.alibaba.fastjson.JSONObject;
import com.whp.usdtfb.Util.utils.JWTUtil;
import com.whp.usdtfb.user.Interface.LFbAssetInterface;
import com.whp.usdtfb.user.Interface.LFbSellInterface;
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
@RequestMapping("LFbAsset")
public class LFbAssetController {

    @Autowired
    private LFbAssetInterface lFbAssetInterface;

    @PostMapping("LFbAssetSelect")
    public JSONObject LFbAssetSelect(HttpServletRequest request) {
        String pid = JWTUtil.getUsername(request.getHeader("Authorization"), "pid");
        JSONObject json = lFbAssetInterface.LFbAssetSelect(pid);
        return json;
    }

    @PostMapping("LFbAssetInsert")
    public JSONObject LFbAssetInsert(@RequestParam Map<String, Object> map, HttpServletRequest request) {
        String pid = JWTUtil.getUsername(request.getHeader("Authorization"), "pid");
        map.put("userid", pid);
        JSONObject json = lFbAssetInterface.LFbAssetInsert(map);
        return json;
    }


}

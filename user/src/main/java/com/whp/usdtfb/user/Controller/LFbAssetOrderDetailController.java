package com.whp.usdtfb.user.Controller;

import com.alibaba.fastjson.JSONObject;
import com.whp.usdtfb.Util.utils.JWTUtil;
import com.whp.usdtfb.user.Interface.LFbAssetOrderDetailInterface;
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
@RequestMapping("LFbAssetOrderDetail")
public class LFbAssetOrderDetailController {

    @Autowired
    private LFbAssetOrderDetailInterface lFbAssetOrderDetailInterface;

    @PostMapping("LFbAssetOrderDetailSelect")
    public JSONObject LFbAssetOrderDetailSelect(@RequestParam Map<String, Object> map, HttpServletRequest request) {
        String pid = JWTUtil.getUsername(request.getHeader("Authorization"), "pid");
        JSONObject json = lFbAssetOrderDetailInterface.LFbAssetOrderDetailSelect(map, pid);
        return json;
    }

}

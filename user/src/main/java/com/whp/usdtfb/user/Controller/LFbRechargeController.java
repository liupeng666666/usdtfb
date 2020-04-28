package com.whp.usdtfb.user.Controller;

import com.alibaba.fastjson.JSONObject;
import com.whp.usdtfb.Util.utils.JWTUtil;
import com.whp.usdtfb.user.Interface.LFbRechargeInterface;
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
@RequestMapping("LFbRecharge")
public class LFbRechargeController {

    @Autowired
    private LFbRechargeInterface lFbRechargeInterface;

    @PostMapping("LFbRechargeSelect")
    public JSONObject LFbRechargeSelect(@RequestParam Map<String, Object> map, HttpServletRequest request) {
        String pid = JWTUtil.getUsername(request.getHeader("Authorization"), "pid");
        JSONObject json = lFbRechargeInterface.LFbRechargeSelect(pid, map);
        return json;
    }

}

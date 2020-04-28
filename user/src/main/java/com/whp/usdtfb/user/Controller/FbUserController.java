package com.whp.usdtfb.user.Controller;

import com.alibaba.fastjson.JSONObject;
import com.whp.usdtfb.Util.utils.JWTUtil;
import com.whp.usdtfb.Util.utils.RedisUtils;
import com.whp.usdtfb.user.Interface.FbUserInterface;
import com.whp.usdtfb.user.utils.Sms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2018/12/18 14:47
 * @descrpition :
 */
@RestController
@RequestMapping("FbUser")
public class FbUserController {

    @Autowired
    private FbUserInterface fbUserInterface;

    @PostMapping("FbUserSelect")
    public JSONObject fbuserselect(HttpServletRequest request) {
        String pid = JWTUtil.getUsername(request.getHeader("Authorization"), "pid");
        JSONObject json = fbUserInterface.FbUserSelect(pid);
        return json;
    }

    @PostMapping("FbUserUpdate")
    public JSONObject fbuserUpdate(@RequestParam Map<String, Object> map, HttpServletRequest request) {
        String pid = JWTUtil.getUsername(request.getHeader("Authorization"), "pid");
        map.put("pid", pid);
        JSONObject json = fbUserInterface.FbUserUpdate(map);
        return json;
    }

    @PostMapping("FbUserSMS")
    public JSONObject FbUserSMS(HttpServletRequest request) {
        String pid = JWTUtil.getUsername(request.getHeader("Authorization"), "pid");
        JSONObject json = fbUserInterface.FbUserSelect(pid);
        if (json.containsKey("user") && json.getJSONObject("user").containsKey("phone")) {
            int code = (int) ((Math.random() * 9 + 1) * 100000);
            try {
                Sms.sendSms(json.getJSONObject("user").getString("phone"), code + "");
                RedisUtils.time_set("sms." + pid, code + "", 8, 900);
                json.put("code", 100);
            } catch (Exception e) {
                e.printStackTrace();
                json.put("code", 103);
            }

        } else {
            json.put("code", 102);
        }
        return json;
    }
}

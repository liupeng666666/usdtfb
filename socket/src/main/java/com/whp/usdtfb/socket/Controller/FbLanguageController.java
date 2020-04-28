package com.whp.usdtfb.socket.Controller;

import com.alibaba.fastjson.JSONObject;
import com.whp.usdtfb.Util.utils.JWTUtil;
import com.whp.usdtfb.socket.Interface.FbLanguageInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2019/1/3 4:41
 * @descrpition :
 */
@RestController
@RequestMapping("FbLanguage")
public class FbLanguageController {
    @Autowired
    private FbLanguageInterface fbLanguageInterface;

    @PostMapping("FbLanguageSelect")
    public JSONObject FbLanguageSelect(HttpServletRequest request) {
        String userid = JWTUtil.getUsername(request.getHeader("Authorization"), "pid");
        JSONObject json = fbLanguageInterface.FbLanguageSelect(userid);
        return json;
    }

    @PostMapping("FbLanguageInsert")
    public JSONObject FbLanguageInsert(@RequestParam Map<String, Object> map, HttpServletRequest request) {
        String userid = JWTUtil.getUsername(request.getHeader("Authorization"), "pid");
        map.put("userid", userid);
        JSONObject json = fbLanguageInterface.FbLanguageInsert(map);
        return json;
    }
}

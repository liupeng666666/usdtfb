package com.whp.usdtfb.user.Controller;

import com.alibaba.fastjson.JSONObject;
import com.whp.usdtfb.Util.utils.ImgUtil;
import com.whp.usdtfb.Util.utils.JWTUtil;
import com.whp.usdtfb.user.Interface.FbPlayInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2018/12/19 14:23
 * @descrpition :
 */
@RestController
@RequestMapping("/FbPlay")
public class FbPlayController {
    @Autowired
    private FbPlayInterface fbPlayInterface;

    @PostMapping("/FbPlaySelect")
    public JSONObject FbPlaySelect(@RequestParam Map<String, Object> map, HttpServletRequest request) {
        String pid = JWTUtil.getUsername(request.getHeader("Authorization"), "pid");
        map.put("pid", pid);
        JSONObject json = fbPlayInterface.FbPlaySelect(map);
        return json;
    }

    @PostMapping("/FbPlayInsert")
    public JSONObject FbPlayInsert(@RequestParam(value = "file", required = false) MultipartFile file, @RequestParam Map<String, Object> map, HttpServletRequest request) {
        String pid = JWTUtil.getUsername(request.getHeader("Authorization"), "pid");
        if (file != null) {
            String url = ImgUtil.url(file, 300, 450, 1);
            map.put("img", url);
        }
        map.put("userid", pid);
        JSONObject json = fbPlayInterface.FbPlayInsert(map);
        return json;
    }

    @PostMapping("/FbPlayUpdate")
    public JSONObject FbPlayUpdate(@RequestParam(value = "file", required = false) MultipartFile file, @RequestParam Map<String, Object> map, HttpServletRequest request) {
        String pid = JWTUtil.getUsername(request.getHeader("Authorization"), "pid");
        if (file != null) {
            String url = ImgUtil.url(file, 300, 450, 1);
            map.put("img", url);
        }
        map.put("userid", pid);
        JSONObject json = fbPlayInterface.FbPlayUpdate(map);
        return json;
    }

    @PostMapping("/FbBankSelect")
    public JSONObject FbBankSelect() {
        JSONObject json = fbPlayInterface.FbBankSelect();
        return json;
    }
}

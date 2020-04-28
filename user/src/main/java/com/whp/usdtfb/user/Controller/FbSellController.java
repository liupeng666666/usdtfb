package com.whp.usdtfb.user.Controller;

import com.alibaba.fastjson.JSONObject;
import com.whp.usdtfb.Util.utils.JWTUtil;
import com.whp.usdtfb.user.Interface.FbSellInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2018/12/26 15:06
 * @descrpition :
 */
@RestController
@RequestMapping("FbSell")
public class FbSellController {

    @Autowired
    private FbSellInterface fbSellInterface;

    @PostMapping("FbSellInsert")
    public JSONObject FbSellInsert(@RequestParam Map<String, Object> map, HttpServletRequest request) {
        String pid = JWTUtil.getUsername(request.getHeader("Authorization"), "pid");
        map.put("userid", pid);
        JSONObject json = fbSellInterface.FbSellInsert(map);
        return json;
    }

    @PostMapping("FbSellSelect")
    public JSONObject FbSellSelect(@RequestParam Map<String, Object> map, HttpServletRequest request) {
        String pid = JWTUtil.getUsername(request.getHeader("Authorization"), "pid");
        map.put("userid", pid);
        JSONObject json = fbSellInterface.FbSellSelect(map);
        return json;
    }

    @PostMapping("FbSellDan")
    public JSONObject FbSellDan(@RequestParam String pid, HttpServletRequest request) {
        String userid = JWTUtil.getUsername(request.getHeader("Authorization"), "pid");
        JSONObject json = fbSellInterface.FbSellDan(pid, userid);
        return json;
    }

    @PostMapping("FbSellUpdate")
    public JSONObject FbSellUpdate(@RequestParam Map<String, Object> map, HttpServletRequest request) {
        String userid = JWTUtil.getUsername(request.getHeader("Authorization"), "pid");
        map.put("userid", userid);
        JSONObject json = fbSellInterface.FbSellUpdate(map);
        return json;
    }

    @PostMapping("FbSellUpdateState")
    public JSONObject FbSellUpdateState(@RequestParam Map<String, Object> map, HttpServletRequest request) {
        String userid = JWTUtil.getUsername(request.getHeader("Authorization"), "pid");
        map.put("userid", userid);
        JSONObject json = fbSellInterface.FbSellUpdateState(map);
        return json;
    }
}

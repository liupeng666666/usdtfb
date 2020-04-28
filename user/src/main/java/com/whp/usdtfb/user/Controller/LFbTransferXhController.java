package com.whp.usdtfb.user.Controller;

import com.alibaba.fastjson.JSONObject;
import com.whp.usdtfb.Util.utils.JWTUtil;
import com.whp.usdtfb.user.Interface.LFbTransferXhInterface;
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
@RequestMapping("LFbTransferXh")
public class LFbTransferXhController {

    @Autowired
    private LFbTransferXhInterface lFbTransferXhInterface;

    @PostMapping("LFbTransferXhSelect")
    public JSONObject LFbTransferXhSelect(@RequestParam Map<String, Object> map, HttpServletRequest request) {
        String pid = JWTUtil.getUsername(request.getHeader("Authorization"), "pid");
        JSONObject json = lFbTransferXhInterface.LFbTransferXhSelect(map, pid);
        return json;
    }

    @PostMapping("LFbTransInsert")
    public JSONObject FbTransInsert(@RequestParam Map<String, Object> map, HttpServletRequest request) {
        String pid = JWTUtil.getUsername(request.getHeader("Authorization"), "pid");
        JSONObject json = lFbTransferXhInterface.FbTransferInsert(map, pid);
        return json;
    }


}

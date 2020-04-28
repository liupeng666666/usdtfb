package com.whp.usdtfb.user.Controller;

import com.alibaba.fastjson.JSONObject;
import com.whp.usdtfb.Util.utils.JWTUtil;
import com.whp.usdtfb.user.Interface.FbFriendInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2019/1/3 13:47
 * @descrpition :
 */
@RestController
@RequestMapping("FbFriend")
public class FbFriendController {
    @Autowired
    private FbFriendInterface fbFriendInterface;

    @PostMapping("FbFriendInsert")
    public JSONObject FbFriendInsert(@RequestParam Map<String, Object> map, HttpServletRequest request) {
        String userid = JWTUtil.getUsername(request.getHeader("Authorization"), "pid");
        map.put("userid", userid);
        JSONObject json = fbFriendInterface.FbFriendInsert(map);
        return json;
    }
}

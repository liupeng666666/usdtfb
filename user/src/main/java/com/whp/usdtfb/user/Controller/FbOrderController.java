package com.whp.usdtfb.user.Controller;

import com.alibaba.fastjson.JSONObject;
import com.whp.usdtfb.Util.utils.JWTUtil;
import com.whp.usdtfb.user.Interface.FbOrderInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2018/12/29 11:38
 * @descrpition :
 */
@RestController
@RequestMapping("FbOrder")
public class FbOrderController {

    @Autowired
    private FbOrderInterface fbOrderInterface;

    @PostMapping("FbOrderInsert")
    public JSONObject FbOrderInsert(@RequestParam Map<String, Object> map, HttpServletRequest request) {
        String userid = JWTUtil.getUsername(request.getHeader("Authorization"), "pid");
        JSONObject json = new JSONObject();
        if (map.containsKey("type")) {
            if ("1".equals(map.get("type"))) {
                map.put("fuserid", userid);
            } else {
                map.put("userid", userid);
            }
            json = fbOrderInterface.FbOrderInsert(map);
        } else {
            json.put("code", 103);
        }
        return json;
    }

    @PostMapping("FbOrderSelect")
    public JSONObject FbOrderSelect(@RequestParam Map<String, Object> map, HttpServletRequest request) {
        String userid = JWTUtil.getUsername(request.getHeader("Authorization"), "pid");
        map.put("userid", userid);
        JSONObject json = fbOrderInterface.FbOrderSelect(map);
        return json;
    }

    @PostMapping("FbOrderUpdate")
    public JSONObject FbOrderUpdate(@RequestParam Map<String, Object> map, HttpServletRequest request) {
        String userid = JWTUtil.getUsername(request.getHeader("Authorization"), "pid");
        map.put("userid", userid);
        JSONObject json = fbOrderInterface.FbOrderUpdate(map);
        return json;
    }

    @PostMapping("FbOrderSelectQ")
    public JSONObject FbOrderSelectQ(@RequestParam("state") int state, @RequestParam("num") int num, @RequestParam("page") int page, HttpServletRequest request) {
        String userid = JWTUtil.getUsername(request.getHeader("Authorization"), "pid");
        JSONObject json = fbOrderInterface.FbOrderSelectQ(userid, state, page, num);
        return json;
    }
}

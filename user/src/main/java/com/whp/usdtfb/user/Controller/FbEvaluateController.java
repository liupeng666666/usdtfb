package com.whp.usdtfb.user.Controller;

import com.alibaba.fastjson.JSONObject;
import com.whp.usdtfb.user.Interface.FbEvaluateInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2019/1/2 16:12
 * @descrpition :
 */
@RestController
@RequestMapping("FbEvaluate")
public class FbEvaluateController {

    @Autowired
    private FbEvaluateInterface fbEvaluateInterface;

    @PostMapping("FbEvaluateInsert")
    public JSONObject FbEvaluateInsert(@RequestParam Map<String, Object> map) {
        JSONObject json = fbEvaluateInterface.FbEvaluateInsert(map);
        return json;
    }
}

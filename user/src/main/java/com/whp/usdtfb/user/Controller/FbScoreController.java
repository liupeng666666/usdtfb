package com.whp.usdtfb.user.Controller;

import com.alibaba.fastjson.JSONObject;
import com.whp.usdtfb.user.Interface.FbScoreInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : 张吉伟
 * @data : 2019/1/2 15:43
 * @descrpition :
 */
@RestController
@RequestMapping("FbScore")
public class FbScoreController {

    @Autowired
    private FbScoreInterface fbScoreInterface;

    @PostMapping("FbScoreSelect")
    public JSONObject FbScoreSelect() {
        JSONObject json = fbScoreInterface.FbScoreSelect();
        return json;
    }
}

package com.whp.usdtfb.user.Controller;

import com.alibaba.fastjson.JSONObject;
import com.whp.usdtfb.user.Interface.FbProblemClassInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : 张吉伟
 * @data : 2019/1/4 17:59
 * @descrpition :
 */
@RestController
@RequestMapping("FbProblemClass")
public class FbProblemClassController {

    @Autowired
    private FbProblemClassInterface fbProblemClassInterface;

    @PostMapping("FbProbleamClassSelect")
    public JSONObject FbProbleamClassSelect() {
        JSONObject json = fbProblemClassInterface.FbProblemSelect();
        return json;
    }
}

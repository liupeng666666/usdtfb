package com.whp.usdtfb.socket.Controller;

import com.alibaba.fastjson.JSONObject;
import com.whp.usdtfb.Util.utils.ImgUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author : 张吉伟
 * @data : 2019/1/3 14:10
 * @descrpition :
 */
@RestController
@RequestMapping("FbSocket")
public class FBSocketController {

    @PostMapping("FbSocketImg")
    public JSONObject FbSocketImg(@RequestParam(value = "file", required = false) MultipartFile file) {
        JSONObject json = new JSONObject();
        try {
            String url = ImgUtil.url(file, 200, 400, 1);
            json.put("code", 100);
            json.put("img", url);
        } catch (Exception e) {
            e.printStackTrace();
            json.put("code", 103);
        }
        return json;

    }
}

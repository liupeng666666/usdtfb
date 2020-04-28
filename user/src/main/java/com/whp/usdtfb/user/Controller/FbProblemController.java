package com.whp.usdtfb.user.Controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.whp.usdtfb.Util.utils.ImgUtil;
import com.whp.usdtfb.Util.utils.JWTUtil;
import com.whp.usdtfb.user.Interface.FbProblemClassInterface;
import com.whp.usdtfb.user.Interface.FbProblemInterface;
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
 * @data : 2019/1/4 17:59
 * @descrpition :
 */
@RestController
@RequestMapping("FbProblem")
public class FbProblemController {

    @Autowired
    private FbProblemInterface fbProblemInterface;

    @PostMapping("FbProbleamSelect")
    public JSONObject FbProbleamSelect(@RequestParam Map<String, Object> map, HttpServletRequest request) {
        String pid = JWTUtil.getUsername(request.getHeader("Authorization"), "pid");
        map.put("userid", pid);
        JSONObject json = fbProblemInterface.FbProblemSelect(map);
        return json;
    }

    @PostMapping("FbProbleamInsert")
    public JSONObject FbProbleamInsert(@RequestParam(value = "files[]", required = false) MultipartFile[] files, @RequestParam Map<String, Object> map, HttpServletRequest request) {
        String pid = JWTUtil.getUsername(request.getHeader("Authorization"), "pid");
        map.put("userid", pid);
        JSONArray array = new JSONArray();
        for (MultipartFile f : files) {

            String url = ImgUtil.url(f, 720, 1080, 1);
            array.add(url);
        }
        map.put("img", array.toString());
        JSONObject json = fbProblemInterface.FbProblemInsert(map);
        return json;
    }

    @PostMapping("FbProbleamDan")
    public JSONObject FbProbleamDan(@RequestParam("pid") String pid) {
        JSONObject json = fbProblemInterface.FbProblemDan(pid);
        return json;
    }
}

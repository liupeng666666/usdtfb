package com.whp.config;

import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author : 张吉伟
 * @data : 2018/5/23 15:43
 * @descrpition :
 */
@ControllerAdvice
public class ErrorConfig {
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    public JSONObject Error() {
        JSONObject json = new JSONObject();
        json.put("code", 500);
        json.put("Mes", "没有权限");
        return json;
    }
}

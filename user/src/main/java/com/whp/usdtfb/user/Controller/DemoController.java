package com.whp.usdtfb.user.Controller;

import com.whp.usdtfb.Util.utils.JWTUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : 张吉伟
 * @data : 2018/12/10 14:11
 * @descrpition :
 */
@RestController
@RequestMapping("/user")
public class DemoController {
    @PostMapping("/demo")
    public String token(HttpServletRequest request) {
        String token = JWTUtil.sign("020bab83cb484e4c87a0adfe1004db81", "123456", "020bab83cb484e4c87a0adfe1004db81", request.getSession().getId());
        return token;
    }

    @PostMapping("/demo1")
    public String token1(HttpServletRequest request) {
        return "123";
    }
}

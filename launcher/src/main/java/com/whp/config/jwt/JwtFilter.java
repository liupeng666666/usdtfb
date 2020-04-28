package com.whp.config.jwt;


import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.whp.usdtfb.Util.utils.PropertiesUtils;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * @author : 张吉伟
 * @data : 2018/5/16 15:15
 * @descrpition :
 */
public class JwtFilter extends BasicHttpAuthenticationFilter {

    private static String ShiroState = PropertiesUtils.KeyValue("shiro.state");

    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String antrorization = req.getHeader("Authorization");
        return antrorization != null;
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorization = httpServletRequest.getHeader("Authorization");
        JWTToken token = new JWTToken(authorization);
        getSubject(request, response).login(token);
        return true;


    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (isLoginAttempt(request, response)) {
            try {
                executeLogin(request, response);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                response401(request, response);
                return false;
            }
        }
        return true;
    }

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        System.out.println("======");
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String sessionId = httpServletRequest.getSession().getId();
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        if (ShiroState.equals("true")) {
            String authorization = httpServletRequest.getHeader("Authorization");
            if (authorization == null || authorization.equals("null")) {
                response401(request, response);
                return false;
            } else {
                DecodedJWT jwt = JWT.decode(authorization);
                Date date = jwt.getExpiresAt();
                System.out.println("存储：" + jwt.getClaim("sessionid").asString());
                System.out.println("传入" + sessionId);
                if (date.getTime() < System.currentTimeMillis()) {
                    System.out.println("===----===");
                    response401(request, response);
                    return false;
                }
//                else if(!jwt.getClaim("sessionid").asString().equals(sessionId)){
//
//                    response401(request, response);
//                    return false;
//                }
            }
        }
        return super.preHandle(request, response);
    }

    private void response401(ServletRequest req, ServletResponse resp) {
        try {
            JSONObject json = new JSONObject();
            json.put("code", 401);
            HttpServletResponse httpServletResponse = (HttpServletResponse) resp;
            PrintWriter out = httpServletResponse.getWriter();
            resp.setContentType("application/json; charset=utf-8");
            out.append(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

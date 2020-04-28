package com.whp.config.jwt;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author : 张吉伟
 * @data : 2018/5/16 15:25
 * @descrpition :
 */
public class JWTToken implements AuthenticationToken {
    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    private String token;

    public JWTToken(String token) {
        this.token = token;
    }

}

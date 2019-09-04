package com.gnjf.shirologin.shiro.jwt;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * 封装授权信息
 */
public class JwtToken implements AuthenticationToken {

    private String token;

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}

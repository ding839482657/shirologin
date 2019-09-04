package com.gnjf.shirologin.shiro;

import com.gnjf.shirologin.pojo.User;
import com.gnjf.shirologin.shiro.jwt.JwtToken;
import com.gnjf.shirologin.utils.JwtUtil;
import io.jsonwebtoken.*;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class UserRealm extends AuthenticatingRealm {
    @Resource
    JwtUtil jwtUtil;

    // 使用JWT替代原生Token
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    //为了这个方法可以执行姐可以进行登录操作
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken atoken) throws AuthenticationException {
        /**
         * 1.获取到token
        * 2.解密token,获取用户名
         * 3.判断用户名是否存在
         *
         */


        String token = (String) atoken.getCredentials();

        try {
            // 解密获得account，用于和数据库进行对比
            //jwtutil 工具类
//           String account = jwtUtil.parseJWT(token).get(JwtUtil.ACCOUNT).toString();
            Claims claims = jwtUtil.parseJWT(token);
            String account = claims.get(JwtUtil.ACCOUNT).toString();
            System.out.println("account:"+account);
            // 帐号为空
            if (account == null) {
                throw new AuthenticationException("Token中帐号为空");
            }
            /*User user = userService.findByMobile(account);
            if (user == null) {
                throw new AuthenticationException("该帐号不存在");
            }*/
            return new SimpleAuthenticationInfo(token, token, getName());
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
            throw new AuthenticationException("JWT 令牌错误:" + e.getMessage());
        } catch (UnsupportedJwtException e) {
            throw new AuthenticationException("JWT 令牌无效:" + e.getMessage());
        } catch (MalformedJwtException e) {
            throw new AuthenticationException("JWT 令牌格式错误:" + e.getMessage());
        } catch (SignatureException e) {
            throw new AuthenticationException("JWT 令牌签名无效:" + e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new AuthenticationException("JWT 令牌过期:" + e.getMessage());

        } catch (Exception e) {
            throw new AuthenticationException("JWT 令牌参数异常:" + e.getMessage());

        }

    }
}

package com.gnjf.shirologin.shiro.jwt;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gnjf.shirologin.utils.Result;
import com.gnjf.shirologin.utils.StatusCode;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 自定义拦截器
 */
public class JwtFilter extends BasicHttpAuthenticationFilter {
    /**
     * 此方法最终返回的都是true，即允许访问
     * 例如有地址地址： GET /user
     * 登入用户和游客看到的内容是不同的， 如果在这里返回了false，请求会被直接拦截，用户看不到任何东西
     * 所以我们在这里返回true，Controller中可以通过 subject.isAuthenticated() 来判断用户是否登入
     *
     * 权限!!!!!!!!!!!!!!!
     * 如果有些资源只有登入用户才能访问，我们只需要在方法上面加上 @RequiresAuthentication 注解即可
     *
     * 但是这样做有一个缺点，就是不能够对GET,POST等请求进行分别过滤鉴权(因为我们重写了官方的方法)，但实际上对应用影响不大
     * <p>
     * 方法主要功能：
     * 1. 检验请求头是否带有 token ((HttpServletRequest) request).getHeader("Token") != null
     * 2. 如果带有 token，执行 shiro 的 login() 方法，将 token 提交到 Realm 中进行检验；如果没有 token，
     * 说明当前状态为游客状态（或者其他一些不需要进行认证的接口）
     * 3. 如果在 token 校验的过程中出现错误，如 token 校验失败，那么我会将该请求视为认证不通过，则
     * 响应错误信息给前端
     */



    /**
     *拦截到资源就会执行此方法
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {

        System.out.println("isAccessAllowed");
        //判断用户是否携带token 如果携带需要认证
        //token用请求头headers发送过来
        if(isLoginAttempt(request,response)){
            //进行登录操作
            try {
                this.executeLogin(request,response);
            } catch (Exception e) {
                e.printStackTrace();
                //判断异常
                // 认证出现异常，传递错误信息
                String msg = e.getMessage();
                // 获取应用异常(该Cause是导致抛出此throwable(异常)的throwable(异常))
                Throwable throwable = e.getCause();
                if (throwable instanceof SignatureVerificationException) {
                    // 该异常为JWT的AccessToken认证失败(Token或者密钥不正确)
                    msg = "Token或者密钥不正确(" + throwable.getMessage() + ")";
                } else if (throwable instanceof TokenExpiredException) {
                    // 该异常为JWT的AccessToken已过期
                    msg = "Token已过期(" + throwable.getMessage() + ")";
                } else {
                    // 应用异常不为空
                    if (throwable != null) {
                        // 获取应用异常msg
                        msg = throwable.getMessage();
                    }
                }
                // Token认证失败直接返回Response信息
                this.response401(response, msg);
                return false;
            }

        }
        //返回false不可以执行我拦截的路径
        return true;
    }

    private void response401(ServletResponse response, String msg) {
        HttpServletResponse servletResponse = WebUtils.toHttp(response);
        servletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        servletResponse.setCharacterEncoding("UTF-8");
        servletResponse.setContentType("application/json; charset=utf-8");
        try (PrintWriter out = servletResponse.getWriter()) {
            Result result = new Result(false, StatusCode.ACCESS_ERROR, "无权访问(Unauthorized):" + msg);
            ObjectMapper objectMapper = new ObjectMapper();
            String data = objectMapper.writeValueAsString(result);
            out.append(data);
        } catch (IOException e) {
            e.printStackTrace();
            //throw new CustomException("直接返回Response信息出现IOException异常:" + e.getMessage());
        }

    }

    /**
     * 进行登陆操作
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {

        //封装token
        JwtToken jwtToken=new JwtToken(getAuthzHeader(request));
        //调用方法,将token 交给shior进行验证(UserRealm)

        //login会调用UserRealm里面的方法doGetAuthenticationInfo
        this.getSubject(request,response).login(jwtToken);
        //没有异常就会返回true

        return true;
    }

    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        String token = getAuthzHeader(request);

        return token!=null;
    }

    /**
     * 解决跨域问题
     *//*
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个OPTIONS请求，这里我们给OPTIONS请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }*/
}

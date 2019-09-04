package com.gnjf.shirologin.utils.filter;

import com.gnjf.shirologin.utils.JwtUtil;
import com.gnjf.shirologin.utils.Result;
import io.jsonwebtoken.Claims;

public class RequireFilter {


    public Boolean checkroles(String token){
        JwtUtil jwtUtil=new JwtUtil();
        Claims claims = jwtUtil.parseJWT(token);
        String id = claims.getId();
        String subject = claims.getSubject();

        System.out.println("id+subject:------------"+id+subject);
        String role = claims.getIssuer();
        if (role.equals("admin")){
            return true;
        }
        return null;

    }
}

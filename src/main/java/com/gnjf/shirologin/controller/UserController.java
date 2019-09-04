package com.gnjf.shirologin.controller;

import com.gnjf.shirologin.pojo.User;
import com.gnjf.shirologin.service.UserService;
import com.gnjf.shirologin.utils.JwtUtil;
import com.gnjf.shirologin.utils.Result;
import com.gnjf.shirologin.utils.StatusCode;
import io.jsonwebtoken.Claims;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    UserService userService;

    @Resource
    JwtUtil jwtUtil;

    @GetMapping("/showid/{id}")
    public  Result showbyid(@PathVariable String id){
        User user = userService.findById(id);
        return new Result<>(true, StatusCode.OK, "查询成功", user);
    }

    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable String id){
        userService.deleteById(id);
        return new Result<>(true, StatusCode.OK, "删除成功");
    }


    @RequestMapping(method = RequestMethod.POST,value = "insert")
    public Result insert(@RequestBody User user) {
        userService.insert(user);
        return new Result(true,StatusCode.OK,"操作成功");
    }

//    @CrossOrigin(origins = "*")
    @PostMapping(value = "/login")
    public Result login(@RequestBody User user,HttpServletRequest request) {

        User newuser = userService.login(user.getUsername(), user.getPassword());
        if (newuser==null){
            return new Result(false,StatusCode.ERROR,"登录失败");
        }
        //登录成功返回token给用户 jwt
        JwtUtil jwtUtil=new JwtUtil();
        String token = jwtUtil.createJWT(newuser.getPassword(),newuser.getUsername(), newuser.getRole());
        Map<String,String> map= new HashMap<>();
        map.put("token",token);

        return new Result(true,StatusCode.OK,"登录成功",map);
    }

    /**
     * 查询全部
     * @RequiresAuthentication
     * 此注解该方法只能在登录下才能访问
     * 如果没有登录认证就只能看列表数据,登陆了就可以看见特殊数据
     * 用户没登陆就会有异常:.UnauthenticatedException
     *
     * 配置中debug=true
     */

    @RequiresAuthentication
    @GetMapping(value = "/show")
    public Result findAll(HttpServletRequest request){

        String Authorization = request.getHeader("Authorization");

        System.out.println("Authorization:---------"+Authorization);
        Claims claims = jwtUtil.parseJWT(Authorization);

        String id = claims.getId();
        String subject = claims.getSubject();

        System.out.println("id+subject:------------"+id+subject);

        String username = claims.get(JwtUtil.ACCOUNT).toString();


        String role=claims.get(JwtUtil.role).toString();

        System.out.println("role:-----------------"+role);


        List<User> list = userService.selectAll();
        // 判断是否认证，如果是认证状态,返回一些特殊的数据
        Map<String, Object > map = new HashMap();
        map.put("list", list);
        //判断是认证的状态   多显示msg
        if (SecurityUtils.getSubject().isAuthenticated()) {
            map.put("msg1", "我是你有token时显示的消息");
            if (role.equals("user")){
                map.put("msg2","我是你的role是user情况写显示的信息  user");
            }else if(role.equals("admin")){
                map.put("msg3","我是你的role是admin情况写显示的信息  admin");
            }else{
                map.put("msg3","我是你的role是null情况写显示的信息  游客");
            }

        }
        return new Result<>(true, StatusCode.OK, "查询成功", map);
    }






}

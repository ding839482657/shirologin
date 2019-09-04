package com.gnjf.shirologin.service;

import com.gnjf.shirologin.mapper.UserMapper;
import com.gnjf.shirologin.pojo.User;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.Id;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {


    @Resource
    UserMapper userMapper;

    @Resource
    RedisTemplate<String, User> redisTemplate;
    /**
     * 不加这个 就会报这个错    本地和远程的类中serialVersionUID 值不一样.
     * java.io.InvalidClassException: com.gnjf.shirologin.pojo.User;
     * local class incompatible: stream classdesc serialVersionUID = -1782900819558514330,
     * local class serialVersionUID = 7036005049812910210
     */
    public static final String USER_REDIS_KEY = "user_";

    public User findById(String id) {
        //获取缓存
        User user = redisTemplate.opsForValue().get(USER_REDIS_KEY + id);
        System.out.println("缓存中查找:");
        if (user == null) {
            user = userMapper.selectByPrimaryKey(id);
            System.out.println("数据库中查找:  " + user.toString());
            //放入缓存
            redisTemplate.opsForValue().set(USER_REDIS_KEY + id, user, 60, TimeUnit.MINUTES);
            System.out.println("0000000000000");
        }
        return user;
    }

    public void deleteById(String id){
        redisTemplate.delete(USER_REDIS_KEY+id);
        userMapper.deleteByPrimaryKey(id);
    }

    public User login(String username, String password) {
        User user=new User();
        user.setUsername(username);
        user.setPassword(password);
        System.out.println( userMapper.selectOne(user));

        return userMapper.selectOne(user);

    }

    public void insert(User user) {
        //调用shiro中的加密方法
        Md5Hash md5Hash=new Md5Hash(user.getPassword());
        //加密次数
        md5Hash.setIterations(1);
        user.setPassword(md5Hash.toString());
        userMapper.insert(user);
    }

    public List<User> selectAll() {
        return userMapper.selectAll();
    }
}

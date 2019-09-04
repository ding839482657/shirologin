package com.gnjf.shirologin.pojo;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "student")
//implements Serializable   redis缓存的数据得是序列化的
public class User implements Serializable {

    @Id
    private Integer id;


    private String username;


    private String password;



    private Integer age;

   private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }


    public Integer getAge() {
        return age;
    }


    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age +
                ", role='" + role + '\'' +
                '}';
    }

    public User(Integer id, String username, String password, Integer age, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.age = age;
        this.role = role;
    }
}

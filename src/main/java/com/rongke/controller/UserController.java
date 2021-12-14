package com.rongke.controller;

import com.rongke.entity.User;
import com.rongke.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    @PostMapping(value = "/register",produces = "application/json;charset=UTF-8")
    public User register(@RequestBody User user){
        return userService.register(user);
    }
    @PostMapping(value = "/login")
    public Object login(@RequestParam("username") String username,@RequestParam("password") String password) throws Exception {
        return userService.login(username,password);
    }
}

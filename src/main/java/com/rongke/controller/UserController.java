package com.rongke.controller;

import com.rongke.entity.User;
import com.rongke.service.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Api("用户管理模块")
@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "用户注册")
    @PostMapping(value = "/register",produces = "application/json;charset=UTF-8")
    @ApiResponses({
            @ApiResponse(code = 200,message = "请求成功"),
            @ApiResponse(code = 2008,message = "账号已存在")
    })
    public User register(@RequestBody @ApiParam(name = "user",value = "用户实体",required = true) User user){
        return userService.register(user);
    }
    @ApiOperation(value = "用户登录")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "username",value = "用户名",dataType = "java.lang.String",required = true),
        @ApiImplicitParam(name = "password",value = "用户密码",dataType = "java.lang.String",required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200,message = "请求成功"),
            @ApiResponse(code = 2003,message = "密码错误"),
            @ApiResponse(code = 2006,message = "账号被锁定"),
            @ApiResponse(code = 2007,message = "账号不存在")
    })
    @PostMapping(value = "/login")
    public User login(@RequestParam("username") String username,@RequestParam("password") String password) throws Exception {
        return userService.login(username,password);
    }
}

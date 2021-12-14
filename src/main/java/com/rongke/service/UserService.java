package com.rongke.service;

import com.rongke.entity.User;

public interface UserService {
    User register(User user);

    /**
     * 首先查询数据库是否存在该用户名，存在再检查该用户是否被锁定
     * 没有锁定检查密码是否正确，密码正确登录成功，返回user信息
     * 密码错误返回抛出异常，密码错误，并使得attempt+1
     * 如果被锁定了，检查当前时间是否大于解锁时间，若大于则将attempt设为0，否则抛出异常，账户被锁定
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    Object login(String username, String password) throws Exception;
}

package com.rongke.service;

import com.alibaba.druid.pool.WrapperAdapter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.rongke.dao.UserMapper;
import com.rongke.entity.User;
import com.rongke.util.AlertException;
import com.rongke.util.ResultCode;
import com.rongke.util.UserStatueUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Wrapper;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService{
    private UserMapper userMapper;
    @Autowired
    public void setUserMapper(UserMapper userMapper){
        this.userMapper = userMapper;
    }

    /**
     * 查询是否已经注册，如果已经注册返回null，否则返回注册信息
     * @param user
     * @return
     */
    @Override
    public User register(User user) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",user.getUsername());
        User exit = userMapper.selectOne(wrapper);
        if (exit == null){
            user.setStatue(UserStatueUtil.USER_ACTIVE);
            user.setAttempt(0);
            user.setTime(System.currentTimeMillis());
            int insert = userMapper.insert(user);
            if (insert < 0){
                return null;
            }else {
                return user;
            }
        }else {
            throw new AlertException(ResultCode.USER_ACCOUNT_ALREADY_EXIST);
        }
    }

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
    @Override
    public Object login(String username, String password) throws Exception {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username)
                .eq("statue",UserStatueUtil.USER_ACTIVE);
        User user = userMapper.selectOne(wrapper);
        if (user == null){
            return null;
        }else {
            if (password.equals(user.getPassword())){
                return user;
            }else {
                if (user.getAttempt() >= 3){
                    UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
                    userUpdateWrapper.eq("username",user.getUsername()).set("statue",UserStatueUtil.USER_LOCKED).set("time",new Date(System.currentTimeMillis()+1000*60*60));
                    userMapper.update(user, userUpdateWrapper);
                    throw new Exception("您的账号被锁定");
                }
                UpdateWrapper<User> updateWrapper  = new UpdateWrapper<>();
                updateWrapper.eq("username",user.getUsername()).set("attempt",user.getAttempt()+1);
                int update = userMapper.update(user, updateWrapper);
                return user;
            }
        }
    }

}

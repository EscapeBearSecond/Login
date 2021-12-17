package com.rongke.service;

import com.alibaba.druid.pool.WrapperAdapter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.rongke.dao.UserMapper;
import com.rongke.entity.User;
import com.rongke.util.AlertException;
import com.rongke.util.ResultCode;
import com.rongke.util.UserStatueUtil;
import com.rongke.util.UserUtils;
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
    @Override
    public User login(String username, String password) throws Exception {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        User user = userMapper.selectOne(wrapper);
        if (user == null){
            throw new AlertException(ResultCode.USER_ACCOUNT_NOT_EXIST);
        }
        //如果状态为0，且没到解锁时间，则抛出账号被锁定异常
        if (user.getStatue() == UserStatueUtil.USER_LOCKED && System.currentTimeMillis() <= user.getTime()){
            throw new AlertException(ResultCode.USER_ACCOUNT_LOCKED);
        }
        //如果状态为0，但是到了解锁时间，则将状态设为1，并且重置尝试次数
        if (user.getStatue() == UserStatueUtil.USER_LOCKED && System.currentTimeMillis() > user.getTime()){
            UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("username",username)
                    .set("attempt",0)
                    .set("statue",UserStatueUtil.USER_ACTIVE);
            userMapper.update(user,updateWrapper);
            user.setAttempt(0);
            user.setStatue(UserStatueUtil.USER_ACTIVE);
        }
        //如果尝试次数大于3，则设置状态为0，解锁时间为1分钟
        if (user.getAttempt() > 3){
            UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("username",username)
                    .set("time",System.currentTimeMillis()+60*1000)
                    .set("statue",UserStatueUtil.USER_LOCKED);
            userMapper.update(user,updateWrapper);
            throw new AlertException(ResultCode.USER_ACCOUNT_LOCKED);
        }

        //密码正确则返回该用户信息
        if (user.getPassword().equals(password)){
            return user;
        }
        //密码不正确，尝试次数加1，抛出密码错误异常
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("username",username)
                .set("attempt",user.getAttempt()+1);
        userMapper.update(user,updateWrapper);
        throw new AlertException(ResultCode.USER_CREDENTIALS_ERROR);

    }

}

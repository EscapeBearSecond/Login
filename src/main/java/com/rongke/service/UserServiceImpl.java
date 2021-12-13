package com.rongke.service;

import com.alibaba.druid.pool.WrapperAdapter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rongke.dao.UserMapper;
import com.rongke.entity.User;
import com.rongke.util.UserStatueUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Wrapper;

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
            int insert = userMapper.insert(user);
            if (insert < 0){
                return null;
            }else {
                return user;
            }
        }else {
            return null;
        }
    }
}

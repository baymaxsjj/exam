package com.baymax.exam.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baymax.exam.common.core.result.Result;
import com.baymax.exam.user.mapper.UserMapper;
import com.baymax.exam.user.model.User;
import com.baymax.exam.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户信息 服务实现类
 * </p>
 *
 * @author baymax
 * @since 2022-10-07
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    UserMapper userMapper;
    /**
     * 添加用户
     *
     * @param user 用户
     * @return {@link Boolean}
     */
    @Override
    public Result<String> addUser(User user) {
        //用户名唯一验证
        User uUser=getUserByUserName(user.getUsername());
        if(uUser!=null){
            return Result.msgInfo("该用户名已存在");
        }
        //邮箱唯一验证
        User eUser=getUserByEmail(user.getEmail());
        if(eUser!=null){
            return Result.msgInfo("邮箱已注册");
        }
        User upUser=new User();
        upUser.setUsername(user.getUsername());
        upUser.setEncodePassword(user.getPassword());
        upUser.setNickname(user.getNickname());
        upUser.setEmail(user.getEmail());
        save(upUser);
        return Result.success();
    }

    /**
     * 更新用户
     *
     * @param user 用户
     * @return {@link Result}<{@link String}>
     */
    @Override
    public Result<String> updateUser(User user) {
        return null;
    }
    public void updatePassword(int id,String password){
        LambdaUpdateWrapper<User> updateWrapper=new LambdaUpdateWrapper();
        updateWrapper.eq(User::getId,id);
        updateWrapper.set(User::getPassword,new BCryptPasswordEncoder().encode(password));
        update(updateWrapper);
    }

    /**
     * 用户名获取用户
     *
     * @param username 用户名
     * @return {@link User}
     */
    @Override
    public User getUserByUserName(String username) {
        return getOne(new QueryWrapper<User>().eq("username",username).last("LIMIT 1"));
    }

    /**
     * 通过电子邮件获取用户
     *
     * @param email 电子邮件
     * @return {@link User}
     */
    @Override
    public User getUserByEmail(String email) {
        return  getOne(new QueryWrapper<User>().eq("email",email).last("LIMIT 1"));
    }
}

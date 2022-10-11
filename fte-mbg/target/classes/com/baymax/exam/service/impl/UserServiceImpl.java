package com.baymax.exam.service.impl;

import com.baymax.exam.model.User;
import com.baymax.exam.mapper.UserMapper;
import com.baymax.exam.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

}

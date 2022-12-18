package com.baymax.exam.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baymax.exam.user.model.UserAuthInfo;
import com.baymax.exam.user.mapper.UserAuthInfoMapper;
import com.baymax.exam.user.service.IUserAuthInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * VIEW 服务实现类
 * </p>
 *
 * @author baymax
 * @since 2022-12-14
 */
@Service
public class UserAuthInfoServiceImpl extends ServiceImpl<UserAuthInfoMapper, UserAuthInfo> implements IUserAuthInfoService {
    public UserAuthInfo getStudentByUserId(int userId){
        LambdaQueryWrapper<UserAuthInfo> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(UserAuthInfo::getUserId,userId);
        return getOne(queryWrapper);
    }
}

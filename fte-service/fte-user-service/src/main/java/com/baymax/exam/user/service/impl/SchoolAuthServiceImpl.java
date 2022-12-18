package com.baymax.exam.user.service.impl;

import com.baymax.exam.user.model.SchoolAuth;
import com.baymax.exam.user.mapper.SchoolAuthMapper;
import com.baymax.exam.user.service.ISchoolAuthService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 学生认证表 服务实现类
 * </p>
 *
 * @author baymax
 * @since 2022-12-14
 */
@Service
public class SchoolAuthServiceImpl extends ServiceImpl<SchoolAuthMapper, SchoolAuth> implements ISchoolAuthService {

}

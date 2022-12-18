package com.baymax.exam.user.service.impl;

import com.baymax.exam.user.model.SchoolStudent;
import com.baymax.exam.user.mapper.SchoolStudentMapper;
import com.baymax.exam.user.service.ISchoolStudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 学校用户认证信息 服务实现类
 * </p>
 *
 * @author baymax
 * @since 2022-12-14
 */
@Service
public class SchoolStudentServiceImpl extends ServiceImpl<SchoolStudentMapper, SchoolStudent> implements ISchoolStudentService {

}

package com.baymax.exam.user.service.impl;

import com.baymax.exam.model.Courses;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baymax.exam.user.mapper.CoursesMapper;
import com.baymax.exam.user.service.ICoursesService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程信息 服务实现类
 * </p>
 *
 * @author baymax
 * @since 2022-10-11
 */
@Service
public class CoursesServiceImpl extends ServiceImpl<CoursesMapper, Courses> implements ICoursesService {

}

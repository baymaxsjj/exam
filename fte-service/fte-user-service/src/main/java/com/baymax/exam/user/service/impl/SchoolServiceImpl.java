package com.baymax.exam.user.service.impl;

import com.baymax.exam.model.School;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baymax.exam.user.mapper.SchoolMapper;
import com.baymax.exam.user.service.ISchoolService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 学校信息 服务实现类
 * </p>
 *
 * @author baymax
 * @since 2022-10-11
 */
@Service
public class SchoolServiceImpl extends ServiceImpl<SchoolMapper, School> implements ISchoolService {

}

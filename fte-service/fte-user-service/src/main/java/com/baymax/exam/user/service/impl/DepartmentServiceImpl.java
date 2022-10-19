package com.baymax.exam.user.service.impl;

import com.baymax.exam.user.model.Department;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baymax.exam.user.mapper.DepartmentMapper;
import com.baymax.exam.user.service.IDepartmentService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 班级信息 服务实现类
 * </p>
 *
 * @author baymax
 * @since 2022-10-11
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {

}

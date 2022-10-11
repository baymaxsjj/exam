package com.baymax.exam.service.impl;

import com.baymax.exam.model.Department;
import com.baymax.exam.mapper.DepartmentMapper;
import com.baymax.exam.service.IDepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 班级信息 服务实现类
 * </p>
 *
 * @author baymax
 * @since 2022-10-07
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {

}

package com.baymax.exam.user.mapper;

import com.baymax.exam.user.model.SchoolStudent;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 学校用户认证信息 Mapper 接口
 * </p>
 *
 * @author baymax
 * @since 2022-12-14
 */
@Mapper
public interface SchoolStudentMapper extends BaseMapper<SchoolStudent> {

}

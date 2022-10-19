package com.baymax.exam.user.mapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baymax.exam.user.model.JoinClass;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baymax.exam.user.model.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author baymax
 * @since 2022-10-13
 */
@Mapper
public interface JoinClassMapper extends BaseMapper<JoinClass> {
    IPage<User>  getJoinClassUser(IPage<User> page, LambdaQueryWrapper<JoinClass> ew);
    JoinClass getJoinByCourseId(Integer courseId,Integer userId);
}

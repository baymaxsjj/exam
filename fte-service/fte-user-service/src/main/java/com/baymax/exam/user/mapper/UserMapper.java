package com.baymax.exam.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baymax.exam.common.core.result.Result;
import com.baymax.exam.model.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户信息 Mapper 接口
 * </p>
 *
 * @author baymax
 * @since 2022-10-07
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}

package com.baymax.exam.center.mapper;

import com.baymax.exam.center.model.Tags;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 题目标签表 Mapper 接口
 * </p>
 *
 * @author baymax
 * @since 2022-10-17
 */
@Mapper
public interface TagsMapper extends BaseMapper<Tags> {
    List<Tags> getCoursePublicTags(Integer courseId, Integer parentId);
}

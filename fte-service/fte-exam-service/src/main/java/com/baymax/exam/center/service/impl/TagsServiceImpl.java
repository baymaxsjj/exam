package com.baymax.exam.center.service.impl;

import com.baymax.exam.center.model.Tags;
import com.baymax.exam.center.mapper.TagsMapper;
import com.baymax.exam.center.service.ITagsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 题目标签表 服务实现类
 * </p>
 *
 * @author baymax
 * @since 2022-10-17
 */
@Service
public class TagsServiceImpl extends ServiceImpl<TagsMapper, Tags> implements ITagsService {
    @Autowired
    TagsMapper tagsMapper;
    /**
     * 获取课程公共标签
     *
     * @param courseId 课程id
     * @param parentId 父id
     * @return {@link List}<{@link Tags}>
     */
    @Override
    public List<Tags> getCoursePublicTags(Integer courseId, Integer parentId) {
        return tagsMapper.getCoursePublicTags(courseId,parentId);
    }
}

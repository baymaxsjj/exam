package com.baymax.exam.center.service;

import com.baymax.exam.center.model.Tags;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 题目标签表 服务类
 * </p>
 *
 * @author baymax
 * @since 2022-10-17
 */
public interface ITagsService extends IService<Tags> {
    /**
     * 获取课程公共标签
     *
     * @param courseId 课程id
     * @param parentId 父id
     * @return {@link List}<{@link Tags}>
     */
    List<Tags> getCoursePublicTags(Integer courseId,Integer parentId);
}

package com.baymax.exam.vo;

import com.baymax.exam.model.Courses;
import com.baymax.exam.model.User;
import lombok.Data;

/**
 * @author ：Baymax
 * @date ：Created in 2022/10/13 17:03
 * @description：课程信息
 * @modified By：
 * @version:
 */
@Data
public class CourseInfoVo extends Courses{
    private User teacher;
}

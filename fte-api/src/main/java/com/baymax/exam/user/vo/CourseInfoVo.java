package com.baymax.exam.user.vo;

import com.baymax.exam.user.model.Courses;
import com.baymax.exam.user.model.User;
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

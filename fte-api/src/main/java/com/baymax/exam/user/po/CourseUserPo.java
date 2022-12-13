package com.baymax.exam.user.po;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @author ：Baymax
 * @date ：Created in 2022/12/1 18:20
 * @description：班级用户
 * @modified By：
 * @version:
 */
@Data
public class CourseUserPo {
    @Schema(description = "班级列表")
    @NotEmpty(message = "班级列表不能为空")
    private Set<Integer> classIds;
    @Schema(description = "课程Id")
    @NotNull(message = "课程Id")
    private Integer courseId;
    @Schema(description = "学生列表")
    private Set<Integer> studentIds;
}

package com.baymax.exam.user.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author ：Baymax
 * @date ：Created in 2022/12/13 21:43
 * @description：学生信息
 * @modified By：
 * @version:
 */
@Data
public class Student {
    private Integer userId;
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "别名")
    private String nickname;
    @Schema(description = "头像")
    private String picture;

    @Schema(description = "工号/学号")
    private String jobNo;
    @Schema(description = "真实姓名")
    private String  realName;

    @Schema(description = "班级名称/部门")
    private String departmentName;
    @Schema(description = "辅导员/部门管理")
    private Integer leaderId;

    @Schema(description = "学校id")
    private Integer schoolId;
    @Schema(description = "学校名称")
    private String schoolName;
}

package com.baymax.exam.user.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.baymax.exam.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * <p>
 * 课程信息
 * </p>
 *
 * @author baymax
 * @since 2022-10-11
 */
@Getter
@Setter
@TableName("ec_courses")
@Schema(name = "Courses", description = "课程信息")
public class Courses extends BaseEntity {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @NotBlank(message = "课程名称不能为空")
    @Length(min = 4,max = 20,message = "课程名称长度4~20")
    @Schema(description = "课程名称")
    private String name;

    @URL(message = "课程封面不合法")
    @NotBlank(message = "课程封面不能为空")
    @Schema(description = "课程封面")
    private String cover;

    @Schema(description = "课程介绍")
    @Length(min = 1,max = 200,message = "课程介绍长度4~20")
    private String introduce;

    @Pattern(regexp = "0|1",message = "公开状态不合法")
    @Schema(description = "是否公开:0：不公开（邀请码）,1公开（搜索）")
    private String isPublic;

    @Pattern(regexp = "^[0-1]",message = "结课状态不合法")
    @Schema(description = "课程状态:0:正常，1：结课")
    private String status;

    @Schema(description = "创建者")
    private Integer userId;


}

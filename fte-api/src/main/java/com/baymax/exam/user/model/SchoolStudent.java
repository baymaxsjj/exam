package com.baymax.exam.user.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 学校用户认证信息
 * </p>
 *
 * @author baymax
 * @since 2022-12-14
 */
@Getter
@Setter
@TableName("es_school_student")
@Schema(name = "SchoolStudent", description = "学校用户认证信息")
public class SchoolStudent implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "工号/学号")
    private String jobNo;

    @Schema(description = "姓名")
    private String realName;

    private String email;

    private String phone;

    @Schema(description = "部门/班级")
    private Integer departmentId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

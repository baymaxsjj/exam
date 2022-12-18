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
 * 学习部门信息
 * </p>
 *
 * @author baymax
 * @since 2022-12-14
 */
@Getter
@Setter
@TableName("es_school_department")
@Schema(name = "SchoolDepartment", description = "学习部门信息")
public class SchoolDepartment implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "logo")
    private String logo;

    @Schema(description = "班级名称/部门")
    private String name;

    @Schema(description = "上级部门")
    private Integer parentId;

    @Schema(description = "辅导员/部门管理")
    private Integer leaderId;

    @Schema(description = "学校id")
    private Integer schoolId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

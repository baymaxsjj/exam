package com.baymax.exam.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 
 * </p>
 *
 * @author baymax
 * @since 2022-10-11
 */
@Getter
@Setter
@TableName("ec_classes")
@Schema(name = "Classes", description = "")
public class Classes implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @NotBlank(message = "班级名称不能为空")
    @Length(min = 3,max = 20,message = "班级名称应在3~20字符")
    @Schema(description = "班级名称")
    private String name;

    @NotNull(message = "课程id不能为空")
    @Schema(description = "课程id")
    private Integer courseId;

    @Schema(description = "老师id/为了方便查找")
    private Integer teacherId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

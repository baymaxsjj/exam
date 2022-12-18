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
 * 学生认证表
 * </p>
 *
 * @author baymax
 * @since 2022-12-14
 */
@Getter
@Setter
@TableName("es_school_auth")
@Schema(name = "SchoolAuth", description = "学生认证表")
public class SchoolAuth implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "用户id")
    private Integer userId;

    @Schema(description = "学生id")
    private Integer studentId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

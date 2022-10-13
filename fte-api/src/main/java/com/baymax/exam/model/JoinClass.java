package com.baymax.exam.model;

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
 * 
 * </p>
 *
 * @author baymax
 * @since 2022-10-13
 */
@Getter
@Setter
@TableName("ec_join_class")
@Schema(name = "JoinClass", description = "")
public class JoinClass implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer classId;

    @Schema(description = "学生id")
    private Integer studentId;

    private LocalDateTime createdAt;
}

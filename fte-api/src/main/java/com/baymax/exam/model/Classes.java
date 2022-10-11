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

    @Schema(description = "班级名称")
    private String name;

    @Schema(description = "老师")
    private Integer userId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

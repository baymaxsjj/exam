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
public class Courses implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "课程名称")
    private String name;

    @Schema(description = "课程封面")
    private String picture;

    @Schema(description = "课程介绍")
    private String introduce;

    @Schema(description = "是否公开:0：不公开（邀请码）,1公开（搜索）")
    private Byte isPublic;

    @Schema(description = "课程状态:0:正常，1：结课")
    private Byte status;

    @Schema(description = "创建者")
    private Integer userId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

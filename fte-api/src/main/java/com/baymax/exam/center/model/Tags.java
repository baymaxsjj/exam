package com.baymax.exam.center.model;

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

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 题目标签表
 * </p>
 *
 * @author baymax
 * @since 2022-10-17
 */
@Getter
@Setter
@TableName("eq_tags")
@Schema(name = "Tags", description = "题目标签表")
public class Tags extends BaseEntity{

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @NotBlank(message = "题目标签不能为空")
    @Length(min = 3,max = 50,message = "标签名称3~50字符")
    @Schema(description = "标签名称")
    private String tag;

    @Schema(description = "课程id")
    @NotNull(message = "课程id不能为空")
    private Integer courseId;

    @Schema(description = "父标签Id,null为顶级标签 ")
    private Integer parentId;

    private Integer teacherId;

}

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

/**
 * <p>
 * 学校信息
 * </p>
 *
 * @author baymax
 * @since 2022-10-11
 */
@Getter
@Setter
@TableName("es_school")
@Schema(name = "School", description = "学校信息")
public class School extends BaseEntity {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "学校名称")
    private String name;

    @Schema(description = "学校域名")
    private String site;

    @Schema(description = "学校介绍")
    private String introduce;
}

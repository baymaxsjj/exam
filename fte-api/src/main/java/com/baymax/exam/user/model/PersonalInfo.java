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
@TableName("es_personal_info")
@Schema(name = "PersonalInfo", description = "学校信息")
public class PersonalInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "工号/学号")
    private String jobNo;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "部门/班级")
    private Integer departmentId;

}

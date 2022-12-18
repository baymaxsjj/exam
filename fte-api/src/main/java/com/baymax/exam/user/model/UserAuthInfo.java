package com.baymax.exam.user.model;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * VIEW
 * </p>
 *
 * @author baymax
 * @since 2022-12-14
 */
@Getter
@Setter
@TableName("es_user_auth_info")
@Schema(name = "StudentInfo", description = "VIEW")
public class UserAuthInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer userId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "别名")
    private String nickname;

    @Schema(description = "头像")
    private String picture;

    private Integer studentId;

    @Schema(description = "工号/学号")
    private String jobNo;

    @Schema(description = "姓名")
    private String realName;

    private String email;

    private String phone;

    private Integer departmentId;

    @Schema(description = "logo")
    private String departmentLogo;

    @Schema(description = "班级名称/部门")
    private String departmentName;

    @Schema(description = "上级部门")
    private Integer parentId;

    @Schema(description = "辅导员/部门管理")
    private Integer leaderId;

    private Integer schoolId;

    private String schoolLogo;

    @Schema(description = "学校名称")
    private String schoolName;

    @Schema(description = "学校域名")
    private String schoolSite;

    @Schema(description = "认证时间")
    private LocalDateTime authTime;
}

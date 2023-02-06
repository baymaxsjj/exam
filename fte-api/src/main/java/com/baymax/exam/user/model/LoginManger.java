package com.baymax.exam.user.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.baymax.exam.user.enums.LoginTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 登录表
 * </p>
 *
 * @author baymax
 * @since 2023-02-06
 */
@Getter
@Setter
@TableName("es_login_manger")
@Schema(name = "LoginManger", description = "登录表")
public class LoginManger implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    @Schema(description = "登录类型")
    private LoginTypeEnum loginType;

    @Schema(description = "登录名")
    private String loginId;

    @Schema(description = "是否启用")
    private LocalDateTime enable;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

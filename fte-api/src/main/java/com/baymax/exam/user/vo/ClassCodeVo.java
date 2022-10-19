package com.baymax.exam.user.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author ：Baymax
 * @date ：Created in 2022/10/14 8:52
 * @description：班级码
 * @modified By：
 * @version:
 */
@Data
public class ClassCodeVo {
    @Schema(description = "班级码")
    private String code;
    @Schema(description = "过期时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expirationTime;
}

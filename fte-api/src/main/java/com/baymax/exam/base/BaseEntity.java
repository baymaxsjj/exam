package com.baymax.exam.base;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Null;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
//    @TableField(fill = FieldFill.INSERT)
//    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

//    @TableField(fill = FieldFill.INSERT_UPDATE)
//    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "修改时间")
    private LocalDateTime updatedAt;
}

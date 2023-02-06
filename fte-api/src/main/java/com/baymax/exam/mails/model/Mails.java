package com.baymax.exam.mails.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 
 * </p>
 *
 * @author Baymax
 * @since 2022-03-13
 */
@Getter
@Setter
@TableName("ee_mails")
@Schema(name = "Mails", description = "")
public class Mails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description ="邮件表")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description ="发送人")
    private String from;

    @Schema(description ="接收人")
    private String to;

    @Schema(description ="邮件主题")
    private String subject;

    @Schema(description ="发送内容")
    private String text;

    @Schema(description ="模板位置")
    private String template;

    @TableField(exist = false)
    @Schema(description ="模板数据")
    private Map<String,Object> data;

    @Schema(description ="抄送")
    private String cc;

    @Schema(description ="密送")
    private String bcc;

    @Schema(description ="发送状态")
    private String status;

    @Schema(description = "错误信息")
    private String error;

    private LocalDateTime createdAt=LocalDateTime.now();
    @JsonIgnore
    private MultipartFile[] multipartFiles;//邮件附件

}

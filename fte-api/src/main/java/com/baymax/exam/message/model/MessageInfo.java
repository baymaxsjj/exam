package com.baymax.exam.message.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.baymax.exam.base.BaseEntity;
import com.baymax.exam.common.core.enums.ClientIdEnum;
import com.baymax.exam.message.enums.MessageTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 
 * </p>
 *
 * @author baymax
 * @since 2022-11-21
 */
@Getter
@Setter
@TableName("em_message_info")
@Schema(name = "MessageInfo", description = "")
@NoArgsConstructor
public class MessageInfo extends BaseEntity {

    public MessageInfo(Integer userId,ClientIdEnum clientId){
        this.userId=userId;
        this.clientId=clientId;
    }

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "介绍")
    private String introduce;

    @Schema(description = "路由地址")
    private String path;
//    @NotNull(message = "消息类型不能为空")
    @Schema(description = "消息类型")
    private MessageTypeEnum type;

    @Schema(description = "用户id为null时，代表target_id的全体消息")
    private Integer userId;

    @Schema(description = "目标id,行为表id")
    private Integer targetId;

    @NotNull(message = "客户端id不能为空")
    @Schema(description = "客户端id")
    private ClientIdEnum clientId;
}

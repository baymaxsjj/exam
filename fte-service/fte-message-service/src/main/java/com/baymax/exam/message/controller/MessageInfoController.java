package com.baymax.exam.message.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baymax.exam.common.core.base.LoginUser;
import com.baymax.exam.common.core.result.PageResult;
import com.baymax.exam.common.core.result.Result;
import com.baymax.exam.message.MessageResult;
import com.baymax.exam.message.enums.MessageTypeEnum;
import com.baymax.exam.message.interceptor.MyWebSocketHandler;
import com.baymax.exam.message.model.MessageInfo;
import com.baymax.exam.message.service.impl.MessageInfoServiceImpl;
import com.baymax.exam.message.service.impl.MessageWebSocketService;
import com.baymax.exam.web.utils.UserAuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author baymax
 * @since 2022-11-21
 */
@Tag(name = "消息服务")
@Validated
@RestController
@RequestMapping("/message-info")
public class MessageInfoController {
    @Autowired
    MyWebSocketHandler myWebSocketHandler;
//    MessageWebSocketService messageWebSocketService;

    @Autowired
    MessageInfoServiceImpl messageInfoService;
    @GetMapping("/list/{type}/{targetId}")
    public Result getMessageInfo(@PathVariable MessageTypeEnum type,
                                 @PathVariable(required = false) String targetId,
                                 @RequestParam(required = false, defaultValue = "1") Long currentPage,
                                 @RequestParam(required = false, defaultValue = "10") Long total,
                                 @RequestParam(required = false, defaultValue = "false") Boolean isAll){
        LambdaQueryWrapper<MessageInfo> queryWrapper=new LambdaQueryWrapper<>();
        Map<SFunction<MessageInfo, ?>, Object> param=new HashMap<>();
        LoginUser user = UserAuthUtil.getUser();
        param.put(MessageInfo::getClientId,user.getClientId());
        param.put(MessageInfo::getType,type);
        param.put(MessageInfo::getUserId,user.getId());
        //如果有的消息没有行为表
        if(targetId!=null){
            param.put(MessageInfo::getTargetId,targetId);
        }
        // 如果先系统消息，用户id 为null
        if(isAll){
            queryWrapper.or().isNull(MessageInfo::getUserId);
        }
        queryWrapper.allEq(param,true);
        Page<MessageInfo> page=new Page(currentPage,total);
        messageInfoService.page(page,queryWrapper);
        return Result.success(PageResult.setResult(page));
    }
    @Operation(summary = "发送消息")
    @PostMapping("/inner/send-message")
    public Boolean sendMessage(@RequestBody @Validated MessageResult message){
        final MessageTypeEnum type = message.getInfo().getType();
        if(type!=null){
            //不是发送数据的，保存消息
            if(type.getValue()>200){
                messageInfoService.save(message.getInfo());
            }
            //系统消息全局发送
            if(type.getValue()>200&&type.getValue()<300){
                myWebSocketHandler.batchMessage(message);
                return true;
            }
        }
        return myWebSocketHandler.sendMessage(message);
    }
    @Operation(summary = "发送批量消息")
    @PostMapping("/inner/batch/send-message")
    public Map<Integer, Boolean> sendBatchMessage(@RequestBody @Validated MessageResult message, @RequestBody Set<Integer> ids){
        return myWebSocketHandler.batchMessage(ids,message);
    }

}

package com.baymax.exam.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baymax.exam.common.core.base.LoginUser;
import com.baymax.exam.message.mapper.MessageInfoMapper;
import com.baymax.exam.message.model.MessageInfo;
import com.baymax.exam.message.service.IMessageInfoService;
import com.baymax.exam.web.utils.UserAuthUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baymax
 * @since 2022-11-21
 */
@Service
public class MessageInfoServiceImpl extends ServiceImpl<MessageInfoMapper, MessageInfo> implements IMessageInfoService {
    public IPage<MessageInfo> getMessageList(int moduleId, int targetId,Integer startId, int currentPage, int pageSize){
        LambdaQueryWrapper<MessageInfo> queryWrapper=new LambdaQueryWrapper<>();
        LoginUser user = UserAuthUtil.getUser();
        queryWrapper.eq(MessageInfo::getTargetId,targetId);
        queryWrapper.eq(MessageInfo::getClientId,user.getClientId());
        //缩小查找范围
        if(startId!=null){
            queryWrapper.lt(MessageInfo::getId,startId);
        }
        queryWrapper.like(MessageInfo::getType,moduleId+"%");
        queryWrapper.orderByDesc(List.of(MessageInfo::getCreatedAt,MessageInfo::getId));
        Page<MessageInfo> page=new Page(currentPage,pageSize);
        return page(page,queryWrapper);
    }
}

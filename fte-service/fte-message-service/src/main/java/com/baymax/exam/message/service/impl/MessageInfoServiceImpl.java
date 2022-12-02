package com.baymax.exam.message.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baymax.exam.message.mapper.MessageInfoMapper;
import com.baymax.exam.message.model.MessageInfo;
import com.baymax.exam.message.service.IMessageInfoService;
import org.springframework.stereotype.Service;

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

}

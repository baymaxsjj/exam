package com.baymax.exam.user.service.impl;

import com.baymax.exam.model.PersonalInfo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baymax.exam.user.mapper.PersonalInfoMapper;
import com.baymax.exam.user.service.IPersonalInfoService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 学校信息 服务实现类
 * </p>
 *
 * @author baymax
 * @since 2022-10-11
 */
@Service
public class PersonalInfoServiceImpl extends ServiceImpl<PersonalInfoMapper, PersonalInfo> implements IPersonalInfoService {

}

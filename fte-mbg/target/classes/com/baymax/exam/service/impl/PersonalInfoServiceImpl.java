package com.baymax.exam.service.impl;

import com.baymax.exam.model.PersonalInfo;
import com.baymax.exam.mapper.PersonalInfoMapper;
import com.baymax.exam.service.IPersonalInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 学校信息 服务实现类
 * </p>
 *
 * @author baymax
 * @since 2022-10-07
 */
@Service
public class PersonalInfoServiceImpl extends ServiceImpl<PersonalInfoMapper, PersonalInfo> implements IPersonalInfoService {

}

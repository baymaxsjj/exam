package com.baymax.exam.user.test;

import com.baymax.exam.user.utils.CourseClassCodeUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author ：Baymax
 * @date ：Created in 2022/12/8 20:25
 * @description：班级码测试
 * @modified By：
 * @version:
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class ClassCodeTest {
    @Autowired
    CourseClassCodeUtil courseClassCodeUtil;
    @Test
    public void generateCode(){
        courseClassCodeUtil.getAvailableCode();
    }
}


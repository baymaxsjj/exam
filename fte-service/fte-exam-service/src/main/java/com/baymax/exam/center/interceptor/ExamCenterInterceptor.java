package com.baymax.exam.center.interceptor;

import com.baymax.exam.center.exceptions.ExamOnLineException;
import com.baymax.exam.center.model.ExamInfo;
import com.baymax.exam.center.service.impl.ExamInfoServiceImpl;
import com.baymax.exam.user.feign.CourseClient;
import com.baymax.exam.user.feign.UserClient;
import com.baymax.exam.user.model.JoinClass;
import com.baymax.exam.web.utils.UserAuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author ：Baymax
 * @date ：Created in 2022/11/1 20:00
 * @description：在线考试认证拦截器
 * @modified By：
 * @version:
 */
@Component
@Slf4j
public class ExamCenterInterceptor implements HandlerInterceptor {
    public static final String EXAM_INFO_KEY="examInfo";
    @Autowired
    ExamInfoServiceImpl examInfoService;
    @Lazy
    @Autowired
    CourseClient courseClient;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<String, String> pathVars = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        String examInfoId = pathVars.get("examInfoId");
        if(examInfoId==null){
            return false;
        }
        log.info(String.valueOf(examInfoService));
        //获取考试信息
        ExamInfo examInfo = examInfoService.getById(examInfoId);
        if(examInfo==null){
            throw new ExamOnLineException("非法请求~");
        }
        LocalDateTime nowTime=LocalDateTime.now();
        Integer courseId = examInfo.getCourseId();
        //试卷id
        Integer userId = UserAuthUtil.getUserId();
        JoinClass joinClass = courseClient.joinCourseByStuId(courseId, userId);
        if(joinClass==null){
            throw new ExamOnLineException("非法请求~");
        }
        if(nowTime.isBefore(examInfo.getStartTime())||nowTime.isAfter(examInfo.getEndTime())){
            throw new ExamOnLineException("不在考试时间~");
        }
        log.info(examInfo.toString());
        request.setAttribute(EXAM_INFO_KEY,examInfo);
        return true;
    }
}

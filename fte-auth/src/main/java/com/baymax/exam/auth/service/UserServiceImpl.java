package com.baymax.exam.auth.service;

import cn.hutool.json.JSONUtil;
import com.baymax.exam.common.core.base.LoginUser;
import com.baymax.exam.auth.base.SecurityUser;
import com.baymax.exam.common.core.base.ExamAuth;
import com.baymax.exam.feign.UserServiceClient;
import com.baymax.exam.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * @author ：Baymax
 * @date ：Created in 2022/10/9 18:41
 * @description：
 * @modified By：
 * @version:
 */
@Slf4j
@Service
public class UserServiceImpl implements UserDetailsService {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    UserServiceClient userServiceClient;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String clientId = request.getParameter("client_id");
        log.info(username);
        LoginUser loginUser=null;
        if(ExamAuth.ADMIN_CLIENT_ID.equals(clientId)){
            //TODO:管理端获取用户

        }else    {
           User user=userServiceClient.findUser(username);
           if(user!=null){
               loginUser=new LoginUser();
               loginUser.setUsername(user.getUsername());
               loginUser.setPassword(user.getPassword());
               loginUser.setId(user.getId());
               loginUser.setRoles(new ArrayList<>());
               loginUser.setEnabled(user.getEnable().equals("0"));
           }
        }
        if (loginUser==null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        loginUser.setClientId(clientId);
        log.info(loginUser.toString());
        SecurityUser securityUser=new SecurityUser(loginUser);
        if (!securityUser.isEnabled()) {
            throw new DisabledException("账号已禁用！");
        } else if (!securityUser.isAccountNonLocked()) {
            throw new LockedException("账号已停用");
        } else if (!securityUser.isAccountNonExpired()) {
            throw new AccountExpiredException("账号已过期");
        } else if (!securityUser.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException("账号异常");
        }
        return securityUser;
    }
}

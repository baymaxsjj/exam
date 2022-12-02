package com.baymax.exam.common.core.base;

import com.baymax.exam.common.core.enums.ClientIdEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author ：Baymax
 * @date ：Created in 2022/10/9 20:49
 * @description：登录用户信息
 * @modified By：
 * @version:
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser {
        private Integer id;
        private String username;
        private String password;
        private Boolean enabled;
        private ClientIdEnum clientId;
        private String ip;
        private List<String> roles;
}

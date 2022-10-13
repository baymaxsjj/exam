package com.baymax.exam.auth.model;

/**
 * @author ：Baymax
 * @date ：Created in 2022/10/9 21:00
 * @description：
 * @modified By：
 * @version:
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 登录用户信息
 * Created by macro on 2020/6/19.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecurityUser implements UserDetails {
    private LoginUser userInfo;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList authorities = new ArrayList<>();
        userInfo.getRoles().forEach(item -> authorities.add(new SimpleGrantedAuthority(item)));
        return authorities;
    }

    @Override
    public String getPassword() {
        return userInfo.getPassword();
    }

    @Override
    public String getUsername() {
        return userInfo.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return userInfo.getEnabled();
    }

}

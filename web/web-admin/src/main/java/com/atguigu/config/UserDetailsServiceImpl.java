package com.atguigu.config;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Admin;
import com.atguigu.service.AdminService;
import com.atguigu.service.PermissionService;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Reference
    private AdminService adminService;

    @Reference
    private PermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查询用户对象
        Admin admin =  adminService.getByUsername(username);
        // 判断用户是否存在
        if (admin == null){
            return null;
        }
        String password = admin.getPassword();
        // 根据用户id，查询用户所有的权限
        List<String> codeList =  permissionService.findCodeListByAdminId(admin.getId());

        List<SimpleGrantedAuthority> list = new ArrayList<>();
        for (String code : codeList) {
            if (StringUtils.isEmpty(code))
                continue;
            list.add(new SimpleGrantedAuthority(code));
        }


        return new User(username,password, list);
    }
}

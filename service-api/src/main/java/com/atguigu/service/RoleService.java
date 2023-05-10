package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.Role;

public interface RoleService extends BaseService<Role> {

    void insertAdminAndRole(Long adminId, Long[] roleIds);
}

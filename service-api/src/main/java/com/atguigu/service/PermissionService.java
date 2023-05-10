package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.Permission;

import java.util.List;
import java.util.Map;

public interface PermissionService extends BaseService<Permission> {
    List<Map<String, Object>> findPermissionByRoleId(Long roleId);

    void insertRoleAndPermission(Long[] permissionIds, Long roleId);

    List<Permission> findMenuPermissionByAdminId(Long adminId);

    List<String> findCodeListByAdminId(Long id);
}

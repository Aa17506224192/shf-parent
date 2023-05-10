package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.PermissionHelper;
import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.PermissionDao;
import com.atguigu.dao.RolePermissionDao;
import com.atguigu.entity.Permission;
import com.atguigu.entity.RolePermission;
import com.atguigu.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.lang.annotation.Retention;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = PermissionService.class)
public class PermissionServiceImpl extends BaseServiceImpl<Permission> implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private RolePermissionDao rolePermissionDao;

    @Override
    protected BaseDao<Permission> getEntityDao() {
        return permissionDao;
    }

    @Override
    public List<Map<String, Object>> findPermissionByRoleId(Long roleId) {
        // 查询所有的权限
        List<Permission> permissionList = permissionDao.findAll();
        // 查询当前角色已经拥有的权限
        List<Long> permissionIdList = rolePermissionDao.findPermissionIdByRoleId(roleId);
        List<Map<String,Object>> zNodes = new ArrayList<>();
        // 迭代的是所有的权限
        for (Permission permission : permissionList) {
//            { id:2, pId:0, name:"随意勾选 2", checked:true, open:true},
            Map<String, Object> map = new HashMap<>();
            map.put("id",permission.getId());
            map.put("pId",permission.getParentId());
            map.put("name",permission.getName());
            // 我已经拥有的权限是否在所有的权限里面
            if (permissionIdList.contains(permission.getId())){
                map.put("checked",true);
            }
            zNodes.add(map);
        }


        return zNodes;
    }

    @Override
    public void insertRoleAndPermission(Long[] permissionIds, Long roleId) {
        rolePermissionDao.deleteByRoleId(roleId);
        for (Long permissionId : permissionIds) {
            if (StringUtils.isEmpty(permissionId))
                continue;
            RolePermission rolePermission = new RolePermission();
            rolePermission.setPermissionId(permissionId);
            rolePermission.setRoleId(roleId);
            rolePermissionDao.insert(rolePermission);
        }
    }

    @Override
    public List<Permission> findMenuPermissionByAdminId(Long adminId) {
        // 根据id查询用户的权限
        List<Permission> permissionList = null;
        // 如果adminid==1，说明当前登录的用户是超级管理员，如果是超级管理员，说明有所有的权限
        if (adminId.longValue()==1){
            permissionList = permissionDao.findAll();
        }else {
            // 如果不是adminid！=1，说明是普通用户，查询普通用户的权限
            permissionList = permissionDao.findListByAdminId(adminId);
        }

        // 把权限列表构建成一颗树
        List<Permission> permissions = PermissionHelper.bulid(permissionList);
        return permissions;
    }

    @Override
    public List<String> findCodeListByAdminId(Long adminId) {
        if (adminId.longValue() == 1){
            return permissionDao.findAllCodeList();
        }
        return permissionDao.findCodeListByAdminId(adminId);
    }
}

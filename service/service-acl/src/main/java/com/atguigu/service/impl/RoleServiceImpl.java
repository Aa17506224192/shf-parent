package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseService;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.AdminRoleDao;
import com.atguigu.dao.RoleDao;
import com.atguigu.entity.AdminRole;
import com.atguigu.entity.Role;
import com.atguigu.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Service(interfaceClass = RoleService.class)
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {

    @Resource
    private RoleDao roleDao;

    @Autowired
    private AdminRoleDao adminRoleDao;


    @Override
    protected BaseDao<Role> getEntityDao() {
        return roleDao;
    }

    /**
     * 用户id：18 角色id ：1
     * 用户id：18 角色id ：8
     *
     *
     *
     * @param adminId
     * @param roleIds
     */
    @Override
    public void insertAdminAndRole(Long adminId, Long[] roleIds) {




        // 先把中间表用户已经拥有的角色给删除
        adminRoleDao.deleteByAdminId(adminId);


        for (Long roleId : roleIds) {
            // 插入数据之前，先判断当前角色id是否有值
            //["编辑","","浏览"]
            if (StringUtils.isEmpty(roleId))
                continue;
            AdminRole adminRole = new AdminRole();
            adminRole.setAdminId(adminId);
            adminRole.setRoleId(roleId);
            adminRoleDao.insert(adminRole);
        }

    }
}

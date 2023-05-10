package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Role;
import com.atguigu.service.PermissionService;
import com.atguigu.service.RoleService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {

    private final static String PAGE_INDEX = "role/index";
    private final static String PAGE_CREATE = "role/create";
    private final static String PAGE_EDIT = "role/edit";
    private final static String PAGE_ASSGIN_SHOW = "role/assginShow";
    private final static String PAGE_SUCCESS = "common/successPage";
    @Reference
    private RoleService roleService;

    @Reference
    private PermissionService permissionService;

    /**
     * 保存权限
     * th:action="@{/role/assignPermission}"
     */
    @RequestMapping("/assignPermission")
    public String assignPermission(Long[] permissionIds,Long roleId){
        permissionService.insertRoleAndPermission(permissionIds,roleId);
        return PAGE_SUCCESS;
    }




    /**
     * 显示权限页面
     * opt.openWin("/role/assignShow/"+id,'修改',580,430);
     */
    @RequestMapping("/assignShow/{roleId}")
    public String assignShow(@PathVariable Long roleId,ModelMap modelMap){
        // 根据角色id，查询用户所有的权限
        List<Map<String,Object>>  zNodes = permissionService.findPermissionByRoleId(roleId);
        modelMap.addAttribute("zNodes",zNodes);
        modelMap.addAttribute("roleId",roleId);


        return PAGE_ASSGIN_SHOW;
    }





    /**
     * 根据id进行删除
     * 思路
     * ① 获取前端传递过来的角色id
     * ② 实现删除操作
     * opt.confirm('/role/delete/'+id);
     */
    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        roleService.delete(id);
        return "redirect:/role";
    }




    /**
     * 更新表单数据
     * 实现思路
     * ① 获取前端传递过来的表单数据
     * ② 调用service里面的更新方法
     * ③ 更新到数据库
     * ④ 更新成功之后，返回成功页面
     * th:action="@{/role/update}"
     */
     @RequestMapping("/update")
     public String update(Role role){
         roleService.update(role);
         return "common/successPage";
     }



    /**
     * 实现思路
     * ① 获取前端传入的id数据
     * ② 根据id进行查询role
     * ③ 获取到的角色，返回给前端
     * opt.openWin('/role/edit/' + id,'修改',580,430);
     */
     @RequestMapping("/edit/{id}")
     public String edit(@PathVariable Long id,ModelMap modelMap){
         // 获取单个对象，以get开头
         Role role =  roleService.getById(id);
         modelMap.addAttribute("role",role);
         return PAGE_EDIT;
     }



    /**
     * 提交表单
     * ① 点击表单，给表单一个点击事件，然后提交到action
     *    th:action="@{/role/save}"
     * ② 后台获取Role角色数据
     * ③ 直接调用service---调用dao，进行保存
     * ④ 页面插入完成之后，需要进行刷新，刷新实际上就是把当前角色页面重新查询一遍
     *
     * 重定向和转发的区别
     * ① 重定向请求2次，会改变
     * ② 转发请求1次，地址不会改变
     */
    @PreAuthorize("hasAuthority('role.create2')")
    @RequestMapping("/save")
    public String save(Role role){
        // 插入数据
        roleService.insert(role);
        // 刷新的动作是框架帮我们做的
        return "common/successPage";
    }


    /**
     * 添加新增方法
     * opt.openWin("/role/create","新增",580,430);
     * @param modelMap
     * @return
     */
    @PreAuthorize("hasAuthority('role.create3')")
    @RequestMapping("/create")
    public String create(ModelMap modelMap){
        return PAGE_CREATE;
    }

    @PreAuthorize("hasAuthority('role.show')")
    @RequestMapping
    public String index(ModelMap  modelMap, HttpServletRequest request){
        // 实现分页的功能
        // 实现分页的时候，必须传当前页码 pageNum，每个页面的大小pageSize
        // 封装一个分页的方法
        Map<String,Object> filters = getFilters(request);
        // 获取所有的数据
//        List<Role> roleList = roleService.findAll();
        // 根据分页查询
        PageInfo<Role> pageInfo =  roleService.findPage(filters);
        modelMap.addAttribute("page",pageInfo);
        modelMap.addAttribute("filters",filters);
        // /WEB-INF/templates/role/index.html
        return PAGE_INDEX;
    }


}

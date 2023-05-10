package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Admin;
import com.atguigu.service.AdminService;
import com.atguigu.service.RoleService;
import com.atguigu.util.QiniuUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

// <a class="J_menuItem" href="/admin" data-index="0">用户管理</a>
@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController {

    private final static String LIST_ACTION = "redirect:/admin";

    private final static String PAGE_INDEX = "admin/index";
    private final static String PAGE_CREATE = "admin/create";
    private final static String PAGE_EDIT = "admin/edit";
    private final static String PAGE_SUCCESS = "common/successPage";

    @Reference
    private AdminService adminService;


    private final static String PAGE_UPLOED_SHOW = "admin/upload";

    private final static String PAGE_ASSGIN_SHOW = "admin/assignShow";

    @Reference
    private RoleService roleService;

    /**
     * th:action="@{/admin/assignRole}"
     * 保存角色
     * adminId : 表示用户id
     * roleIds ： 表示用户选中的角色
     */
    @RequestMapping("/assignRole")
    public String assignRole(Long adminId,Long[] roleIds){
        roleService.insertAdminAndRole(adminId,roleIds);
        return PAGE_SUCCESS;
    }




    /**
     * 显示角色页面
     * opt.openWin('/admin/assignShow/'+id,'分配角色',550,450)
     * @return
     */
    @RequestMapping("/assignShow/{adminId}")
    public String assignShow(@PathVariable Long adminId,ModelMap modelMap){
        // 根据adminId，查询所有的角色
        Map<String,Object> roleMap =  adminService.findRoleByAdminId(adminId);


//        // 所有的角色
//        ArrayList<Object> noAssginRoleList = new ArrayList<>();
//        // 已经选择的角色
//        ArrayList<Object> assginRoleList = new ArrayList<>();
        modelMap.addAllAttributes(roleMap);
        modelMap.addAttribute("adminId",adminId);
        return PAGE_ASSGIN_SHOW;
    }


    @GetMapping("/uploadShow/{id}")
    public String uploadShow(ModelMap model,@PathVariable Long id) {
        model.addAttribute("id", id);
        return PAGE_UPLOED_SHOW;
    }

    @PostMapping("/upload/{id}")
    public String upload(@PathVariable Long id, @RequestParam(value = "file") MultipartFile file, HttpServletRequest request) throws IOException {
        try {
            String newFileName =  UUID.randomUUID().toString() ;
            // 上传图片
            QiniuUtils.upload2Qiniu(file.getBytes(),newFileName);
            String url= "http://ru829shg9.hn-bkt.clouddn.com/"+ newFileName;
            Admin admin = new Admin();
            admin.setId(id);
            admin.setHeadUrl(url);
            adminService.update(admin);
            return PAGE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @RequestMapping
    public String index(ModelMap modelMap, HttpServletRequest request){



        // 获取前端传递过来的参数
        // 查看参数的快捷键 ： ctrl + p
        Map<String, Object> filters = getFilters(request);
        // 实现分页
        PageInfo<Admin> page = adminService.findPage(filters);
        // 返回的数据，key必须跟前端参数保持一致，不然获取不了数据
        modelMap.addAttribute("page",page);
        // 这个参数目的是为了回显数据
        modelMap.addAttribute("filters",filters);
        return PAGE_INDEX;
    }
    /**
     * 进入新增页面
     * @return
     */
    @GetMapping("/create")
    public String create() {
        return PAGE_CREATE;
    }

    /**
     * 保存新增
     * @param admin
     * @return
     */
    @PostMapping("/save")
    public String save(Admin admin) {
        //设置默认头像
//        admin.setHeadUrl("http://47.93.148.192:8080/group1/M00/03/F0/rBHu8mHqbpSAU0jVAAAgiJmKg0o148.jpg");
        String password = admin.getPassword();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encode = encoder.encode(password);
        admin.setPassword(encode);
        adminService.insert(admin);

        return PAGE_SUCCESS;
    }

    /**
     * 进入编辑页面
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/edit/{id}")
    public String edit(ModelMap model,@PathVariable Long id) {
        Admin admin = adminService.getById(id);
        model.addAttribute("admin",admin);
        return PAGE_EDIT;
    }

    /**
     * 保存更新
     * @return
     */
    @PostMapping(value="/update")
    public String update(Admin admin) {

        adminService.update(admin);

        return PAGE_SUCCESS;
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        adminService.delete(id);
        return LIST_ACTION;
    }

}

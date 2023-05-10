package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Admin;
import com.atguigu.entity.HouseBroker;
import com.atguigu.service.AdminService;
import com.atguigu.service.HouseBrokerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

//opt.openWin('/houseBroker/create?houseId=[[${house.id}]]','新增经纪人',630,300)
@Controller
@RequestMapping("/houseBroker")
public class HouseBrokerController {

    @Reference
    private HouseBrokerService houseBrokerService;

    @Reference
    private AdminService adminService;

    private final static String LIST_ACTION = "redirect:/house/";
    private final static String PAGE_CREATE = "houseBroker/create";
    private final static String PAGE_EDIT = "houseBroker/edit";
    private final static String PAGE_SUCCESS = "common/successPage";


    /**
     * opt.confirm('/houseBroker/delete/[[${house.id}]]/'+id);
     */
    @RequestMapping("/delete/{houseId}/{houseBrokerId}")
    public String delete(@PathVariable("houseId") Long houseId,@PathVariable("houseBrokerId") Long houseBrokerId){
        houseBrokerService.delete(houseBrokerId);
        return LIST_ACTION + houseId;
    }


    /**
     * th:action="@{/houseBroker/update}"
     */
    //更新经纪人
    @RequestMapping("/update")
    public String update(HouseBroker houseBroker){
        //根据HouseBroker对象中经纪人的id获取经纪人对象
        Admin admin = adminService.getById(houseBroker.getBrokerId());
        //将经纪人的名字，头像地址设置到houseBroker对象中
        houseBroker.setBrokerName(admin.getName());
        houseBroker.setBrokerHeadUrl(admin.getHeadUrl());
        //调用HouseBrokerService中更新的方法
        houseBrokerService.update(houseBroker);
        //去common下的成功页面
        return "common/successPage";
    }


    /**
     * 编辑经纪人
     * opt.openWin('/houseBroker/edit/' + id,'修改经纪人',630,300);
     */
    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Map modelMap){
        // 根据经纪人id查询经纪人对象
        HouseBroker houseBroker = houseBrokerService.getById(id);
        List<Admin> adminList = adminService.findAll();
        modelMap.put("adminList",adminList);
        modelMap.put("houseBroker",houseBroker);
        return PAGE_EDIT;

    }




    /**
     * 保存经纪人
     * action="/houseBroker/save"
     * @return
     */
    @RequestMapping("/save")
    public String save(HouseBroker houseBroker){
        // 根据经纪人id，查询出来工作人员
        Admin admin = adminService.getById(houseBroker.getBrokerId());

        // 设置经纪人的名字
        houseBroker.setBrokerName(admin.getName());
        // 设置经纪人的头像
        houseBroker.setBrokerHeadUrl(admin.getHeadUrl());
        houseBrokerService.insert(houseBroker);
        return PAGE_SUCCESS;
    }




    @RequestMapping("/create")
    public String create(@RequestParam("houseId") Long houseId, ModelMap modelMap){
        List<Admin> adminList = adminService.findAll();
        modelMap.addAttribute("houseId",houseId);
        modelMap.addAttribute("adminList",adminList);
        return PAGE_CREATE;
    }
}

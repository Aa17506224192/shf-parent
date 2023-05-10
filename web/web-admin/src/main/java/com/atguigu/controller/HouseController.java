package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.*;
import com.atguigu.service.*;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//<a class="J_menuItem" href="/house" data-index="0">房源管理</a>
@Controller
@RequestMapping("/house")
public class HouseController extends BaseController {

    private final static String LIST_ACTION = "redirect:/house";

    private final static String PAGE_INDEX = "house/index";
    private final static String PAGE_SHOW = "house/show";
    private final static String PAGE_CREATE = "house/create";
    private final static String PAGE_EDIT = "house/edit";
    private final static String PAGE_SUCCESS = "common/successPage";

    @Reference
    private HouseService houseService;

    @Reference
    private CommunityService communityService;

    @Reference
    private DictService dictService;

    @Reference
    private HouseBrokerService houseBrokerService;

    @Reference
    private HouseUserService houseUserService;

    @Reference
    private HouseImageService houseImageService;

    /**
     * http://localhost:8080/xxxx?name=abc
     * http://localhost:8080/xxxx/abc
     * window.location = "/house/" + id;
     * 根据id获取房子信息
     * @RequestParam ： 获取请求参数，设置一个默认值，可以取别名，必须要设置值
     * @PathVariable ： 提取URL后面的参数
     * @RequestBody : 表示前端传输过来的对象和json数据进行互转
     */
    @RequestMapping("/{id}")
    public String show(@PathVariable Long id,ModelMap modelMap){
        // 根据房屋id，获取房屋信息
        House house = houseService.getById(id);
        // 根据房子信息，获取小区id，在根据小区id，获取小区信息
        Community community = communityService.getById(house.getCommunityId());
        //1：房源图片 2：房产图片
//        List<HouseImage> houseImage1List = new ArrayList<>();
//        List<HouseImage> houseImage2List = new ArrayList<>();
        List<HouseImage> houseImage1List = houseImageService.findList(id,1);
        List<HouseImage> houseImage2List = houseImageService.findList(id,2);
        // 房屋中介，经纪人
//        List<HouseBroker> houseBrokerList = new ArrayList<>();
        // 查询房屋经纪人
        List<HouseBroker> houseBrokerList = houseBrokerService.findListByHouseId(id);
        // 房东
//        List<HouseUser> houseUserList = new ArrayList<>();
        List<HouseUser> houseUserList = houseUserService.findListByHouseId(id);

        modelMap.addAttribute("house",house);
        modelMap.addAttribute("community",community);
        modelMap.addAttribute("houseImage1List",houseImage1List);
        modelMap.addAttribute("houseImage2List",houseImage2List);
        modelMap.addAttribute("houseBrokerList",houseBrokerList);
        modelMap.addAttribute("houseUserList",houseUserList);
        return PAGE_SHOW;
    }







    /**
     * 发布和未发布
     *  opt.confirm("/house/publish/" + id + "/" + status, "确定该操作吗？");
     */
    @RequestMapping("/publish/{id}/{status}")
    public String publish(@PathVariable Long id , @PathVariable Integer status){
        houseService.publish(id,status);
        return LIST_ACTION;
    }





    /**
     * opt.confirm('/house/delete/'+id);
     */
    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        houseService.delete(id);
        return LIST_ACTION;
    }


    /**
     * 编辑
     * @param model
     * @param id
     * @return
     */
    @RequestMapping("/edit/{id}")
    public String edit(ModelMap model,@PathVariable Long id) {
        House house = houseService.getById(id);
        model.addAttribute("house",house);

        model.addAttribute("communityList",communityService.findAll());
        model.addAttribute("houseTypeList",dictService.findListByDictCode("houseType"));
        model.addAttribute("floorList",dictService.findListByDictCode("floor"));
        model.addAttribute("buildStructureList",dictService.findListByDictCode("buildStructure"));
        model.addAttribute("directionList",dictService.findListByDictCode("direction"));
        model.addAttribute("decorationList",dictService.findListByDictCode("decoration"));
        model.addAttribute("houseUseList",dictService.findListByDictCode("houseUse"));
        return PAGE_EDIT;
    }

    /**
     * 保存更新
     * @return
     */
    @RequestMapping(value="/update")
    public String update(House house) {

        houseService.update(house);

        return PAGE_SUCCESS;
    }


    /**
     *
     * 保存房源
     * th:action="@{/house/save}"
     */
    @RequestMapping("/save")
    public String save(House house){
        // 设置房屋的状态 1 ：发布 0 ：没有发布，中介只是录入了房子，但是用户看不了
        house.setStatus(0);
        houseService.insert(house);
        return PAGE_SUCCESS;
    }


    /**
     * 显示新增页面
     * opt.openWin('/house/create','新增',630,430)
     * @return
     */
    @RequestMapping("/create")
    public String create(ModelMap modelMap){
        // 获取所有的小区
        modelMap.addAttribute("communityList",communityService.findAll());
        // 户型
        modelMap.addAttribute("houseTypeList",dictService.findListByDictCode("houseType"));
        // 楼层
        modelMap.addAttribute("floorList",dictService.findListByDictCode("floor"));
        // 建筑结构
        modelMap.addAttribute("buildStructureList",dictService.findListByDictCode("buildStructure"));
        // 朝向
        modelMap.addAttribute("directionList",dictService.findListByDictCode("direction"));
        // 装修
        modelMap.addAttribute("decorationList",dictService.findListByDictCode("decoration"));
        // 房屋用途
        modelMap.addAttribute("houseUseList",dictService.findListByDictCode("houseUse"));

        return PAGE_CREATE;
    }



    @RequestMapping
    public String index(HttpServletRequest request , ModelMap modelMap){
        Map<String, Object> filters = getFilters(request);
        PageInfo<House> page = houseService.findPage(filters);
        // 获取所有的小区
        modelMap.addAttribute("communityList",communityService.findAll());
        // 户型
        modelMap.addAttribute("houseTypeList",dictService.findListByDictCode("houseType"));
        // 楼层
        modelMap.addAttribute("floorList",dictService.findListByDictCode("floor"));
        // 建筑结构
        modelMap.addAttribute("buildStructureList",dictService.findListByDictCode("buildStructure"));
        // 朝向
        modelMap.addAttribute("directionList",dictService.findListByDictCode("direction"));
        // 装修
        modelMap.addAttribute("decorationList",dictService.findListByDictCode("decoration"));
        // 房屋用途
        modelMap.addAttribute("houseUseList",dictService.findListByDictCode("houseUse"));



        modelMap.addAttribute("filters",filters);
        modelMap.addAttribute("page",page);
        return PAGE_INDEX;
    }
}

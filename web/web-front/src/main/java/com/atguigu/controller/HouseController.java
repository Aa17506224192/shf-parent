package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.*;
import com.atguigu.result.Result;
import com.atguigu.service.*;
import com.atguigu.vo.HouseQueryVo;
import com.atguigu.vo.HouseVo;
import com.github.pagehelper.PageInfo;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/house")
public class HouseController extends BaseController {

    @Reference
    private HouseService houseService;

    @Reference
    private HouseBrokerService houseBrokerService;

    @Reference
    private UserFollowService userFollowService;

    @Reference
    private HouseImageService houseImageService;
    @Reference
    private CommunityService communityService;
    // 根据房屋id，查询房子的基本信息
//     axios.get('/house/info/'+this.id).then(function (response) {

    /**
     * 实现思路：
     * ① house ：通过房屋id，查询房屋
     * ② 根据房屋对象，房屋对象里面有一个小区id，根据小区id，查找小区对象
     * ③ 根据房屋id，找到中介对象，然后获取中介对象里面的第一个数据
     * ④ 根据房屋id，查询图片对象
     * axios.get('/house/info/'+this.id).then(function (response) {
     *           that.house = response.data.data.house
     *           that.community = response.data.data.community
     *           that.houseBroker = response.data.data.houseBrokerList[0]
     *           that.houseImage1List = response.data.data.houseImage1List
     *           that.isFollow = response.data.data.isFollow
     *           that.isLoad = true
     *         });
     */
    @RequestMapping("/info/{houseId}")
    public Result info(@PathVariable Long houseId, ModelMap modelMap, HttpServletRequest request){
        // 通过房屋id，查询房屋
        House house = houseService.getById(houseId);
        // 根据房屋对象，房屋对象里面有一个小区id，根据小区id，查找小区对象
        Community community = communityService.getById(house.getCommunityId());
        // 根据房屋id，找到中介对象，然后获取中介对象里面的第一个数据
        List<HouseBroker> houseBrokers = houseBrokerService.findListByHouseId(houseId);
        //1：房源图片 2：房产图片
        List<HouseImage> houseImages = houseImageService.findList(houseId, 1);
        // 第一次进入房屋详情，所有的房子肯定没有被关注
        Boolean isFollow = false;
//      通过用户id和房屋id，查询关注表里面，如果有数据表示关注当前房屋，如果没有数据说明没有关注当前房子
        UserInfo userInfo = (UserInfo) request.getSession().getAttribute("USER");
        if (null != userInfo){
            // 如果userinfo不为null，说明当前用户已经登录
            Long userInfoId = userInfo.getId();
            // 查询关注表
            isFollow =  userFollowService.isFollowed(userInfoId,houseId);
        }


        modelMap.addAttribute("house",house);
        modelMap.addAttribute("community",community);
        modelMap.addAttribute("houseBrokerList",houseBrokers);
        modelMap.addAttribute("houseImage1List",houseImages);
        modelMap.addAttribute("isFollow",isFollow);
        return Result.ok(modelMap);

    }


//    axios.post('/house/list/'+pageNum+'/'+this.page.pageSize, this.houseQueryVo).then( (response) =>{
    @RequestMapping("/list/{pageNum}/{pageSize}")
    public Result findListPage(@PathVariable Integer pageNum,
                               @PathVariable Integer pageSize,
                               @RequestBody HouseQueryVo houseQueryVo){
       PageInfo<HouseVo> pageInfo =  houseService.findListPage(pageNum,pageSize,houseQueryVo);
       return Result.ok(pageInfo);
    }
}

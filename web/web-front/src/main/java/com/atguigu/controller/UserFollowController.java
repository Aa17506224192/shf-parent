package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.UserInfo;
import com.atguigu.result.Result;
import com.atguigu.result.ResultCodeEnum;
import com.atguigu.service.UserFollowService;
import com.atguigu.vo.UserFollowVo;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/userFollow")
public class UserFollowController {

//    axios.get('/userFollow/auth/follow/'+this.id)
    @Reference
    private UserFollowService userFollowService;

    /**
     * 根据房屋id，取消房屋
     * axios.get('/userFollow/auth/cancelFollow/'+id)
     */
    @RequestMapping("/auth/cancelFollow/{id}")
    public Result cancelFollow(@PathVariable("id") Long id,HttpServletRequest request){
        userFollowService.cancelFollow(id);
        return Result.ok();
    }



    /**
     * 我关注的所有房源，并且分页
     *  axios.get('/userFollow/auth/list/'+pageNum+'/'+this.page.pageSize)
     */
    @RequestMapping("/auth/list/{pageNum}/{pageSize}")
    public Result findListPage(@PathVariable Integer pageNum,@PathVariable Integer pageSize,HttpServletRequest request){
        UserInfo userInfo = (UserInfo) request.getSession().getAttribute("USER");
        if (userInfo != null){
            // 说明当前用户已经登录
            Long userInfoId = userInfo.getId();
            PageInfo<UserFollowVo> page =  userFollowService.findListPage(pageNum,pageSize,userInfoId);
            return Result.ok(page);
        }
        return Result.build(null,ResultCodeEnum.LOGIN_AUTH);
    }


    /**
     * 我的关注
     * @param request
     * @return
     */
    @RequestMapping("/auth/follow/{houseId}")
    public Result follow(@PathVariable("houseId") Long houseId, HttpServletRequest request){
        // 获取用户对象
        UserInfo user = (UserInfo) request.getSession().getAttribute("USER");
        if (user != null){
            // 关注房屋，实际上是一个插入的过程
            Long userId = user.getId();
            userFollowService.follow(userId,houseId);
            return Result.ok();
        }
        // 用户等于null，说明没有登录，跳转到登录页面
        return Result.build(null,ResultCodeEnum.LOGIN_AUTH);
    }
}

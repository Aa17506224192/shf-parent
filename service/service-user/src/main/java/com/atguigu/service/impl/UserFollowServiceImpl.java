package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.UserFollowDao;
import com.atguigu.entity.UserFollow;
import com.atguigu.service.DictService;
import com.atguigu.service.UserFollowService;
import com.atguigu.vo.UserFollowVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service(interfaceClass = UserFollowService.class)
public class UserFollowServiceImpl  extends BaseServiceImpl<UserFollow> implements UserFollowService {

    @Autowired
    private UserFollowDao userFollowDao;

    @Reference
    private DictService dictService;

    @Override
    protected BaseDao getEntityDao() {
        return userFollowDao;
    }

    @Override
    public void follow(Long userId, Long houseId) {
        UserFollow userFollow = new UserFollow();
        userFollow.setUserId(userId);
        userFollow.setHouseId(houseId);
        userFollowDao.insert(userFollow);
    }

    @Override
    public Boolean isFollowed(Long userInfoId, Long houseId) {
        Integer count = userFollowDao.isFollowed(userInfoId,houseId);
        if (count.intValue()==0){
            return false;
        }
        return true;
    }

    @Override
    public PageInfo<UserFollowVo> findListPage(Integer pageNum, Integer pageSize, Long userInfoId) {
        PageHelper.startPage(pageNum,pageSize);
        // 根据userId，查询房屋
        Page<UserFollowVo> page = userFollowDao.findListPage(userInfoId);
        // 获取所有的房屋
        List<UserFollowVo> list = page.getResult();
        for (UserFollowVo userFollowVo : list) {
            // 户型
            String houseTypeName = dictService.getNameById(userFollowVo.getHouseTypeId());
            // 楼层
            String floorName = dictService.getNameById(userFollowVo.getFloorId());
            // 朝向
            String directionName = dictService.getNameById(userFollowVo.getDirectionId());
            userFollowVo.setHouseTypeName(houseTypeName);
            userFollowVo.setFloorName(floorName);
            userFollowVo.setDirectionName(directionName);

        }
        return new PageInfo<UserFollowVo>(page,10);
    }

    @Override
    public void cancelFollow(Long id) {
        userFollowDao.delete(id);
    }
}

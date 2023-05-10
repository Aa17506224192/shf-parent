package com.atguigu.dao;

import com.atguigu.base.BaseDao;
import com.atguigu.entity.UserFollow;
import com.atguigu.vo.UserFollowVo;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

public interface UserFollowDao extends BaseDao<UserFollow> {
    Integer isFollowed(@Param("userId") Long userInfoId,@Param("houseId") Long houseId);

    Page<UserFollowVo> findListPage(Long userInfoId);
}

package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.CommunityDao;
import com.atguigu.dao.DictDao;
import com.atguigu.entity.Community;
import com.atguigu.service.CommunityService;
import com.atguigu.util.CastUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = CommunityService.class)
public class CommunityServiceImpl extends BaseServiceImpl<Community> implements CommunityService {

    @Autowired
    private CommunityDao communityDao;

    @Autowired
    private DictDao dictDao;

    @Override
    protected BaseDao<Community> getEntityDao() {
        return communityDao;
    }


    @Override
    public Community getById(Serializable id) {
        // 根据小区id获取小区对象
        Community community = communityDao.getById(id);
        if (community == null)
            return null;
        // 根据区域id和板块id，查询区域的名称和板块名称
        String areaName = dictDao.getNameById(community.getAreaId());
        String plateName = dictDao.getNameById(community.getPlateId());
        community.setAreaName(areaName);
        community.setPlateName(plateName);
        return community;
    }

    /**
     * 对父类的分页进行增强
     * ① 调用分页的findPage方法，获取的是小区数据Community
     * ② 根据区域id，查询字典里面的区域名字
     * ③ 把区域的名字和板块的名字，设置道小区对象
     * @param filters
     * @return
     */
    @Override
    public PageInfo<Community> findPage(Map<String, Object> filters) {
        int pageSize = CastUtil.castInt(filters.get("pageSize"), 2);
        int pageNum = CastUtil.castInt(filters.get("pageNum"), 1);
        PageHelper.startPage(pageNum,pageSize);
        List<Community> communities = communityDao.findPage(filters);
        for (Community community : communities) {
            // 获取小区的区域id
            Long areaId = community.getAreaId();
            // 根据区域id，查询字典里面的区域名称
            String areaName =  dictDao.getNameById(areaId);
            Long plateId = community.getPlateId();
            String plateName = dictDao.getNameById(plateId);
            community.setAreaName(areaName);
            community.setPlateName(plateName);
        }


        return new PageInfo<Community>(communities,10);
    }
}

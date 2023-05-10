package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.DictDao;
import com.atguigu.dao.HouseDao;
import com.atguigu.entity.House;
import com.atguigu.service.HouseService;
import com.atguigu.vo.HouseQueryVo;
import com.atguigu.vo.HouseVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;

@Service(interfaceClass = HouseService.class)
public class HouseServiceImpl extends BaseServiceImpl<House> implements HouseService {
    @Autowired
    private HouseDao houseDao;
    @Autowired
    private DictDao dictDao;
    @Override
    protected BaseDao<House> getEntityDao() {
        return houseDao;
    }

    @Override
    public House getById(Serializable id) {
        House house = houseDao.getById(id);
        house.setFloorName(dictDao.getNameById(house.getFloorId()));
        house.setHouseTypeName(dictDao.getNameById(house.getHouseTypeId()));
        house.setBuildStructureName(dictDao.getNameById(house.getBuildStructureId()));
        house.setDirectionName(dictDao.getNameById(house.getDirectionId()));
        house.setDecorationName(dictDao.getNameById(house.getDecorationId()));
        house.setHouseUseName(dictDao.getNameById(house.getHouseUseId()));
        return house;
    }

    @Override
    public void publish(Long id, Integer status) {
        // 前端已经判断了当前状态，后端直接输入数据
        House house = new House();
        house.setStatus(status);
        house.setId(id);
        // 更新房子的状态
        houseDao.update(house);
    }

    @Override
    public PageInfo<HouseVo> findListPage(Integer pageNum, Integer pageSize, HouseQueryVo houseQueryVo) {
        // 分页
        PageHelper.startPage(pageNum,pageSize);
        Page<HouseVo> page =  houseDao.findListPage(houseQueryVo);
        // 获取房屋的结果集
        List<HouseVo> list = page.getResult();
        for (HouseVo houseVo : list) {
            // 户型
            String houseTypeName = dictDao.getNameById(houseVo.getHouseTypeId());
           // 楼层
            String floorName =  dictDao.getNameById(houseVo.getFloorId());
           // 朝向directionName
            String directionName = dictDao.getNameById(houseVo.getDirectionId());
            houseVo.setHouseTypeName(houseTypeName);
            houseVo.setFloorName(floorName);
            houseVo.setDirectionName(directionName);
        }
        return new PageInfo<HouseVo>(page,10);
    }
}

package com.atguigu.dao;

import com.atguigu.base.BaseDao;
import com.atguigu.entity.Dict;

import java.util.List;

public interface DictDao extends BaseDao<Dict> {
    List<Dict> findListByParentId(Long id);

    Integer countIsParentId(Long id);

    Dict getByDictCode(String dictCode);

    String getNameById(Long areaId);
}

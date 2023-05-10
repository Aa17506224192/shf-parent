package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.DictDao;
import com.atguigu.entity.Dict;
import com.atguigu.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class DictServiceImpl implements DictService {

    @Autowired
    private DictDao dictDao;

    @Override
    public List<Dict> findListByParentId(Long parentId) {
        return dictDao.findListByParentId(parentId);
    }

    @Override
    public List<Dict> findListByDictCode(String dictCode) {
        // 根据dictcode查询字典
        Dict dict = dictDao.getByDictCode(dictCode);
        if (null ==  dict)
            return null;
        return this.findListByParentId(dict.getId());
    }

    @Override
    public List<Map<String, Object>> findZnodes(Long id) {
        // 这个地方是返回树节点的数据
        // 在返回树控件的时候，必须得遵循树控件的数据格式
        // [{ id:‘自己的id’,name:‘全部分类’, isParent:true}]
        // 根据父id查询所有的节点
        List<Dict> dictList = dictDao.findListByParentId(id);
        List<Map<String, Object>> list = new ArrayList<>();
        // 获取数据之后，需要对拿到的数据做一个拼装
        for (Dict dict : dictList) {
            // 根据字典的id，查询当前的数据是否有count，如果count大于0，有孩子，如果count小于0，没有孩子
           Integer count =  dictDao.countIsParentId(dict.getId());
           // 需要把所有的数据封装到map集合里面
            Map<String, Object> map = new HashMap<>();
            map.put("id",dict.getId());
            map.put("name",dict.getName());
            map.put("isParent",count>0?true:false);
            list.add(map);
        }
        return list;
    }

    @Override
    public String getNameById(Long id) {
        String name = dictDao.getNameById(id);
        return name;
    }
}

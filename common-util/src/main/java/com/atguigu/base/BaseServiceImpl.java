package com.atguigu.base;

import com.atguigu.util.CastUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Transactional
public abstract class BaseServiceImpl<T> implements BaseService<T>{

    protected abstract BaseDao<T> getEntityDao();

    public void insert(T t){
        getEntityDao().insert(t);
    }

    public List<T> findAll(){
        return getEntityDao().findAll();
    }

    public T getById(Serializable id){
       return getEntityDao().getById(id);
    }

    public void update(T t){
        getEntityDao().update(t);
    }

    public void delete(Serializable id){
        getEntityDao().delete(id);
    }

    public PageInfo<T> findPage(Map<String, Object> filters){
        int pageSize = CastUtil.castInt(filters.get("pageSize"), 2);
        int pageNum = CastUtil.castInt(filters.get("pageNum"), 1);
        // 这个是分页的方法
        PageHelper.startPage(pageNum,pageSize);
        return new PageInfo<T>(getEntityDao().findPage(filters),10);
    }



}

package com.atguigu.base;

import com.github.pagehelper.PageInfo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface BaseService<T> {
    List<T> findAll();

    void insert(T t);

    T getById(Serializable id);

    void update(T role);

    void delete(Serializable id);

    PageInfo<T> findPage(Map<String, Object> filters);
}

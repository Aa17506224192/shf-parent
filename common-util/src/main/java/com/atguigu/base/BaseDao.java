package com.atguigu.base;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface BaseDao<T> {
    /**
     * 查询所有
     */
    List<T> findAll();

    void insert(T t);

    T getById(Serializable id);

    void update(T t);

    void delete(Serializable id);

    List<T> findPage(Map<String, Object> filters);
}

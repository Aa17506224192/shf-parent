package com.atguigu.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 我们在进行网络传输的时候，需要传输二进制，所以需要序列化
 */
public class BaseEntity implements Serializable {

    private Long id;
    private Date createTime;
    private Date updateTime;
    private Integer isDeleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}

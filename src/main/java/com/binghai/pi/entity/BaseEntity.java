package com.binghai.pi.entity;


import com.binghai.pi.utils.TimeTools;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * 具有支付状态的实体请继承 PayBizEntity
 * */

@MappedSuperclass
public abstract class BaseEntity implements Serializable {
    private Long created;
    private String createdTime;

    public BaseEntity() {
        created = TimeTools.currentTS();
        createdTime = TimeTools.format(created);
    }


    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public abstract Long getId();
}


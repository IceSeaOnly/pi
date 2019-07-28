package com.binghai.pi.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author huaishuo
 * @date 2019/7/28 下午4:21
 **/
@Entity
@Data
public class RelayTask extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;
    private Long relayId;
    @Column(columnDefinition = "TEXT")
    private String jsonContext;
    private String name;
    private String type;
    private Boolean valid;
}

package com.binghai.pi.entity;

import com.binghai.pi.utils.TimeTools;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author huaishuo
 * @date 2019/8/10 下午5:31
 **/
@Entity
@Data
public class NatureTask extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;
    private Integer runCount;
    private String name;
    /*
     * 列表 格式：RELAY_ID,STATUS,TASK_AFTER_NOW
     * 样例：
     * 1,ON,5000
     * 1,OFF,15000
     * ...
     * **/
    @Column(columnDefinition = "TEXT")
    private String jsonArrayContext;
    /*
     * 上一个任务执行完后再次计划
     * **/
    private Boolean repeatTask;
    private Boolean stop;
    /**
     * 下次计划间隔
     */
    private Long intermission;
    private Long lastExecuteTs;
    private String lastExecuteTime;

    public void setLastExecuteTs(Long lastExecuteTs) {
        this.lastExecuteTs = lastExecuteTs;
        this.lastExecuteTime = TimeTools.format(lastExecuteTs);
    }
}

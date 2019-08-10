package com.binghai.pi.service;

import com.binghai.pi.entity.Relay;
import com.binghai.pi.entity.RelayTask;

import java.util.List;

/**
 * @author huaishuo
 * @date 2019/7/21 下午4:54
 **/
public interface RelayTaskService {
    List<RelayTask> findAllValid();
    List<RelayTask> findAllValidByRelay(Long relayId);

    void saveTask(RelayTask task);

    void remove(Long relayId, Long taskId);

    void updateTask(RelayTask task);
}

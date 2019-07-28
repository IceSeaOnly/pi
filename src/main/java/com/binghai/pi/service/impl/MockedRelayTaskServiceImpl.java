package com.binghai.pi.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.binghai.pi.entity.RelayTask;
import com.binghai.pi.service.RelayTaskService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author huaishuo
 * @date 2019/7/28 下午9:49
 **/
@Component
@ConditionalOnProperty(prefix = "ice", name = "env", havingValue = "dev")
public class MockedRelayTaskServiceImpl implements RelayTaskService {
    @Override
    public List<RelayTask> findAllValid() {
        List<RelayTask> mocks = new ArrayList<>();
        mocks.add(mock(0L));
        mocks.add(mock(0L));
        mocks.add(mock(0L));
        return mocks;
    }

    private RelayTask mock(Long relayId) {
        RelayTask task = new RelayTask();
        task.setRelayId(relayId);
        task.setValid(Boolean.TRUE);
        task.setId(0L);
        task.setType("MOCK");
        task.setName(UUID.randomUUID().toString().substring(30));
        return task;
    }

    @Override
    public List<RelayTask> findAllValidByRelay(Long relayId) {
        return findAllValid();
    }

    @Override
    public void saveTask(RelayTask task) {
        System.out.println(JSONObject.toJSONString(task));
    }

    @Override
    public void remove(Long relayId, Long taskId) {

    }

}

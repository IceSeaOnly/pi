package com.binghai.pi.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.binghai.pi.entity.RelayTask;
import com.binghai.pi.enums.RuleResult;
import com.binghai.pi.rules.RuleExecutor;
import com.binghai.pi.rules.context.RuleContext;
import com.binghai.pi.service.BaseService;
import com.binghai.pi.service.RelayService;
import com.binghai.pi.service.RelayTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author huaishuo
 * @date 2019/7/28 下午4:42
 **/
@Service
//@ConditionalOnProperty(prefix = "ice", name = "env", havingValue = "prod")
public class RelayTaskServiceImpl extends BaseService<RelayTask> implements RelayTaskService {
    @Override
    public List<RelayTask> findAllValid() {
        RelayTask valid = new RelayTask();
        valid.setValid(Boolean.TRUE);
        return query(valid);
    }

    @Override
    public List<RelayTask> findAllValidByRelay(Long relayId) {
        RelayTask task = new RelayTask();
        task.setValid(Boolean.TRUE);
        task.setRelayId(relayId);
        return query(task);
    }

    @Override
    @Transactional
    public void saveTask(RelayTask task) {
        save(task);
    }

    @Override
    @Transactional
    public void remove(Long relayId, Long taskId) {
        RelayTask task = findById(taskId);
        task.setValid(Boolean.FALSE);
        update(task);
    }

    @Override
    @Transactional
    public void updateTask(RelayTask task) {
        update(task);
    }
}

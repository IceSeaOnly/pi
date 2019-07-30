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
@ConditionalOnProperty(prefix = "ice", name = "env", havingValue = "prod")
public class RelayTaskServiceImpl extends BaseService<RelayTask> implements RelayTaskService {
    @Autowired
    private RelayService relayService;
    @Autowired
    private RuleExecutor ruleExecutor;

    @Override
    public List<RelayTask> findAllValid() {
        RelayTask valid = new RelayTask();
        valid.setValid(Boolean.TRUE);
        return query(valid);
    }

    @Scheduled(cron = "0/5 * * * * ?")
    @Transactional
    public void secondTriger() {
        List<RelayTask> tasks = findAllValid();
        for (RelayTask task : tasks) {
            RuleContext context = JSONObject.parseObject(task.getJsonContext(), RuleContext.class);
            if (ruleExecutor.isValid(context)) {
                RuleResult result = ruleExecutor.execute(context);
                if (null != result) {
                    logger.info("{} executed,result = {}", context.getTaskName(), result);
                    switch (result) {
                        case FLIP:
                            relayService.flip(task.getRelayId());
                            break;
                        case ON:
                            relayService.on(task.getRelayId());
                            break;
                        case OFF:
                            relayService.off(task.getRelayId());
                            break;
                    }
                    task.setValid(Boolean.FALSE);
                    update(task);
                    logger.info("{} consumed", context.getTaskName());
                }
            } else {
                task.setValid(Boolean.FALSE);
                update(task);
                logger.info("{} invalid", context.getTaskName());
            }
        }
        logger.info("task scanned.");
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
}

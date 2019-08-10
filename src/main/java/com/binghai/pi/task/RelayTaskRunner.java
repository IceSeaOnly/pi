package com.binghai.pi.task;

import com.alibaba.fastjson.JSONObject;
import com.binghai.pi.entity.RelayTask;
import com.binghai.pi.enums.RuleResult;
import com.binghai.pi.rules.RuleExecutor;
import com.binghai.pi.rules.context.RuleContext;
import com.binghai.pi.service.RelayService;
import com.binghai.pi.service.RelayTaskService;
import com.binghai.pi.utils.BaseBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.Thread.sleep;

/**
 * @author huaishuo
 * @date 2019/8/10 上午9:52
 **/
@Component
@EnableScheduling
public class RelayTaskRunner extends BaseBean implements InitializingBean {
    private ConcurrentHashMap<Long, RelayTask> taskMap = new ConcurrentHashMap<>();

    @Autowired
    private RelayService relayService;
    @Autowired
    private RuleExecutor ruleExecutor;
    @Autowired
    private RelayTaskService relayTaskService;

    @Scheduled(cron = "0/30 * * * * ?")
    public void syncFromDb() {
        taskMap.clear();
        List<RelayTask> tasks = relayTaskService.findAllValid();
        if (isEmptyList(tasks)) {
            logger.info("empty relay task list.");
            return;
        }

        tasks.forEach(t -> taskMap.put(t.getId(), t));
    }

    private void runTask() {
        taskMap.values().parallelStream().forEach(task -> {
            RuleContext context = JSONObject.parseObject(task.getJsonContext(), RuleContext.class);
            if (task.getValid() && ruleExecutor.isValid(context)) {
                RuleResult result = ruleExecutor.execute(context);
                logger.info("{} execute result {}", context.getTaskName(), result);
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
                    relayTaskService.updateTask(task);
                    taskMap.remove(task.getId());
                    logger.info("{} consumed", context.getTaskName());
                }
            } else {
                task.setValid(Boolean.FALSE);
                relayTaskService.updateTask(task);
                taskMap.remove(task.getId());
                logger.info("{} invalid", context.getTaskName());
            }
        });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(() -> {
            while (true) {
                try {
                    runTask();
                    sleep(1000);
                } catch (Exception e) {
                    logger.error("error in task thread.", e);
                }
            }
        }).start();
        syncFromDb();
    }
}

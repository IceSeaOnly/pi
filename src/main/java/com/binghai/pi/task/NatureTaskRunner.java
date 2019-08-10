package com.binghai.pi.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.binghai.pi.entity.NatureTask;
import com.binghai.pi.entity.RelayTask;
import com.binghai.pi.rules.context.OrderOffContext;
import com.binghai.pi.rules.context.OrderOnContext;
import com.binghai.pi.service.NatureTaskService;
import com.binghai.pi.service.RelayTaskService;
import com.binghai.pi.utils.BaseBean;
import com.binghai.pi.utils.TimeTools;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @author huaishuo
 * @date 2019/8/10 下午5:36
 **/
@Component
public class NatureTaskRunner extends BaseBean {
    @Autowired
    private NatureTaskService natureTaskService;
    @Autowired
    private RelayTaskRunner relayTaskRunner;
    @Autowired
    private RelayTaskService relayTaskService;

    @Scheduled(cron = "0 * * * * ?")
    public void loadFromDb() {
        List<NatureTask> taskList = natureTaskService.findAll(999);
        if (isEmptyList(taskList)) {
            return;
        }

        taskList.forEach(task -> handle(task, false));
    }

    public void handle(NatureTask task, boolean forceRun) {
        if (!forceRun) {
            if (task.getStop() || task.getLastExecuteTs() + task.getIntermission() > now()) {
                //不到执行时间
                return;
            }
        }

        task.setRunCount(task.getRunCount() + 1);
        task.setLastExecuteTs(now());
        task.setStop(!task.getRepeatTask());
        natureTaskService.update(task);

        List<String> jobs = Arrays.asList(task.getJsonArrayContext().split(";"));

        if (!checkJobs(jobs)) {
            logger.error("check job failed, cancel execute {}", task.getName());
            return;
        }

        jobs.forEach(job -> {
            job = job.trim();

            String[] parts = job.split(",");
            long ts = now() + Long.parseLong(parts[2]);
            Long relayId = Long.parseLong(parts[0]);
            RelayTask cmd = new RelayTask();

            if (parts[1].equals("ON")) {
                OrderOnContext on = new OrderOnContext(task.getName() + "定时开", Boolean.FALSE, ts);
                cmd.setType("定时开");
                cmd.setName(TimeTools.format(ts) + task.getName() + "定时开");
                cmd.setRelayId(relayId);
                cmd.setJsonContext(JSONObject.toJSONString(on, SerializerFeature.WriteClassName));
                cmd.setValid(Boolean.TRUE);

            } else if (parts[1].equals("OFF")) {
                OrderOffContext off = new OrderOffContext(task.getName() + "定时关", Boolean.FALSE, ts);
                cmd.setType("定时关");
                cmd.setName(TimeTools.format(ts) + task.getName() + "定时关");
                cmd.setRelayId(relayId);
                cmd.setJsonContext(JSONObject.toJSONString(off, SerializerFeature.WriteClassName));
                cmd.setValid(Boolean.TRUE);
            }

            relayTaskService.saveTask(cmd);
        });

        relayTaskRunner.syncFromDb();
    }

    public boolean checkJobs(List<String> jobs) {
        for (String job : jobs) {
            if (!check(job)) {
                return false;
            }
        }
        return true;
    }

    private boolean check(String job) {
        job = job.trim();

        if (hasEmptyString(job)) {
            return false;
        }

        String[] parts = job.split(",");
        if (null == parts || parts.length != 3) {
            return false;
        }

        if (!NumberUtils.isDigits(parts[0])) {
            logger.error("first part must be a relayId");
            return false;
        }

        if (!"ON".equals(parts[1]) && !"OFF".equals(parts[1])) {
            logger.error("secord part must be ON or OFF");
            return false;
        }

        if (!NumberUtils.isDigits(parts[2]) || Long.parseLong(parts[2]) < 3000) {
            logger.error("third part must be a number and bigger not less than 3000!");
            return false;
        }

        return true;
    }
}

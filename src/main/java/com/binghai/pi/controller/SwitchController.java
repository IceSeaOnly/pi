package com.binghai.pi.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.binghai.pi.entity.RelayTask;
import com.binghai.pi.rules.OrderOff;
import com.binghai.pi.rules.RuleExecutor;
import com.binghai.pi.rules.context.OrderOffContext;
import com.binghai.pi.rules.context.OrderOnContext;
import com.binghai.pi.rules.context.RuleContext;
import com.binghai.pi.service.RelayService;
import com.binghai.pi.service.RelayTaskService;
import com.binghai.pi.utils.TimeTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author huaishuo
 * @date 2019/7/3 下午5:41
 **/
@Controller
@RequestMapping("switch")
public class SwitchController extends BaseController {
    @Autowired
    private RuleExecutor ruleExecutor;
    @Autowired
    private RelayService relayService;
    @Autowired
    private RelayTaskService taskService;

    @GetMapping("opt")
    public Object opt(@RequestParam Long relayId, ModelMap map) {
        map.put("relay", relayService.get(relayId));
        map.put("rules", taskService.findAllValidByRelay(relayId));
        return "opt";
    }

    @GetMapping("list")
    public Object list(ModelMap map) {
        map.put("relayList", relayService.list());
        return "list";
    }

    @GetMapping("flip")
    public Object flip(@RequestParam Long relayId, ModelMap map) {
        relayService.flip(relayId);
        return "redirect:list";
    }

    @GetMapping("on")
    public Object on(@RequestParam Long relayId) {
        relayService.on(relayId);
        return success();
    }

    @GetMapping("off")
    public Object off(@RequestParam Long relayId) {
        relayService.off(relayId);
        return success();
    }

    @PostMapping("addRule")
    public Object addRule(@RequestBody Map map) {
        String contextName = getString(map, "_contextName");
        Long relayId = getLong(map, "_relayId");

        RuleContext ctx = ruleExecutor.createContext(contextName, map);
        RelayTask task = new RelayTask();
        task.setJsonContext(JSONObject.toJSONString(ctx));
        task.setRelayId(relayId);
        task.setValid(Boolean.TRUE);

        taskService.saveTask(task);
        return success();
    }

    @GetMapping("deleteTask")
    public String deleteTask(@RequestParam Long taskId, @RequestParam Long relayId) {
        taskService.remove(relayId, taskId);
        return "redirect:opt?relayId=" + relayId;
    }

    @GetMapping("selfChooseDate")
    public String selfChooseDate() {
        return "selfChooseDate";
    }

    @GetMapping("executeMinsWait")
    public String executeMinsWait(@RequestParam Integer type, @RequestParam Integer mins, @RequestParam Long relayId) {
        long target = Long.valueOf(mins) * 60 * 1000 + now();
        String ds = TimeTools.piFormat(target);
        RelayTask task = new RelayTask();
        String timeDesc = TimeTools.formatWithoutSecond(target);
        if (type == 0) {
            relayService.on(relayId);
            OrderOffContext off = new OrderOffContext(timeDesc + "定时关", Boolean.FALSE, ds);
            task.setType("定时关");
            task.setName(timeDesc + "定时关");
            task.setRelayId(relayId);
            task.setJsonContext(JSONObject.toJSONString(off, SerializerFeature.WriteClassName));
            task.setValid(Boolean.TRUE);
        } else {
            relayService.off(relayId);
            OrderOnContext on = new OrderOnContext(timeDesc + "定时开", Boolean.FALSE, ds);
            task.setType("定时开");
            task.setName(timeDesc + "定时开");
            task.setRelayId(relayId);
            task.setJsonContext(JSONObject.toJSONString(on, SerializerFeature.WriteClassName));
            task.setValid(Boolean.TRUE);
        }
        taskService.saveTask(task);
        return "redirect:opt?relayId=" + relayId;
    }
}

package com.binghai.pi.controller;

import com.alibaba.fastjson.JSONArray;
import com.binghai.pi.entity.NatureTask;
import com.binghai.pi.service.NatureTaskService;
import com.binghai.pi.task.NatureTaskRunner;
import com.binghai.pi.utils.BaseBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author huaishuo
 * @date 2019/8/10 下午5:56
 **/
@Controller
@RequestMapping("/nature/")
public class NatureTaskController extends BaseBean {
    @Autowired
    private NatureTaskRunner natureTaskRunner;
    @Autowired
    private NatureTaskService natureTaskService;

    @GetMapping("home")
    public String home(ModelMap map) {
        map.put("list", natureTaskService.findAll(999));
        return "natureHome";
    }

    @GetMapping("detail")
    public String detail(@RequestParam Long taskId, ModelMap map) {
        NatureTask task = natureTaskService.findById(taskId);
        map.put("task", task);
        return "natureDetail.html";
    }

    @GetMapping("status")
    public String status(@RequestParam Long id) {
        NatureTask task = natureTaskService.findById(id);
        task.setLastExecuteTs(now());
        task.setStop(!task.getStop());
        natureTaskService.update(task);
        return "redirect:home";
    }

    @GetMapping("delete")
    public String delete(@RequestParam Long id) {
        natureTaskService.delete(id);
        return "redirect:home";
    }

    @GetMapping("loop")
    public String loop(@RequestParam Long id) {
        NatureTask task = natureTaskService.findById(id);
        task.setLastExecuteTs(now());
        task.setRepeatTask(!task.getRepeatTask());
        natureTaskService.update(task);
        return "redirect:detail?taskId="+id;
    }

    @GetMapping("fire")
    public String fire(@RequestParam Long id) {
        NatureTask task = natureTaskService.findById(id);
        natureTaskRunner.handle(task, true);
        return "redirect:detail?taskId="+id;
    }

    @ResponseBody
    @PostMapping("add")
    public String add(@RequestParam Boolean repeat,
                      @RequestParam String name,
                      @RequestParam String json,
                      @RequestParam Long intermission) {
        if (hasEmptyString(repeat, name, json, intermission)) {
            return "NOT OK";
        }

        List<String> jobs = Arrays.asList(json.split(";"));

        if (natureTaskRunner.checkJobs(jobs)) {
            NatureTask task = new NatureTask();
            task.setRunCount(0);
            task.setStop(true);
            task.setIntermission(intermission);
            task.setLastExecuteTs(now());
            task.setName(name);
            task.setRepeatTask(repeat);
            task.setJsonArrayContext(json);
            natureTaskService.save(task);
        } else {
            return "NOT OK";
        }
        return "OK";
    }
}

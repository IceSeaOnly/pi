package com.binghai.pi.rules.context;

/**
 * @author huaishuo
 * @date 2019/7/28 下午3:48
 **/
public abstract class RuleContext {
    public String taskName;
    private Boolean repeat;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Boolean getRepeat() {
        return repeat;
    }

    public void setRepeat(Boolean repeat) {
        this.repeat = repeat;
    }

    public RuleContext(String taskName, Boolean repeat) {
        this.taskName = taskName;
        this.repeat = repeat;
    }
}

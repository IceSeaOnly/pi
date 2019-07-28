package com.binghai.pi.rules.context;

import lombok.Data;

/**
 * @author huaishuo
 * @date 2019/7/28 下午3:50
 **/
@Data
public class OrderOnContext extends RuleContext {
    private String time;

    public OrderOnContext(String taskName, Boolean repeat, String time) {
        super(taskName, repeat);
        this.time = time;
    }
}

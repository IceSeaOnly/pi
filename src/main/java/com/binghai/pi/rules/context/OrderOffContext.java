package com.binghai.pi.rules.context;

import lombok.Data;

/**
 * @author huaishuo
 * @date 2019/7/28 下午3:50
 **/
@Data
public class OrderOffContext extends RuleContext {
    private Long ts;

    public OrderOffContext(String taskName, Boolean repeat, Long ts) {
        super(taskName, repeat);
        this.ts = ts;
    }
}

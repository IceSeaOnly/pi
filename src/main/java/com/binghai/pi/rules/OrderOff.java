package com.binghai.pi.rules;

import com.alibaba.fastjson.JSONObject;
import com.binghai.pi.enums.RuleResult;
import com.binghai.pi.rules.context.OrderOffContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author huaishuo
 * @date 2019/7/28 下午3:45
 **/
@Component
public class OrderOff extends BaseRelayRule<OrderOffContext> {

    @Override
    public RuleResult evaluate(OrderOffContext context) {
        if (runnable(context)) {
            return RuleResult.OFF;
        }
        return null;
    }

    @Override
    public String name() {
        return "预约关";
    }

    @Override
    public String contextName() {
        return OrderOffContext.class.getSimpleName();
    }

    @Override
    public boolean valid(OrderOffContext context) {
        return now() - buffer < context.getTs();
    }

    @Override
    public boolean runnable(OrderOffContext context) {
        return now() > context.getTs() && now() - buffer < context.getTs();
    }

    @Override
    public OrderOffContext create(Map map) {
        return new JSONObject(map).toJavaObject(OrderOffContext.class);
    }
}

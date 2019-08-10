package com.binghai.pi.rules;

import com.alibaba.fastjson.JSONObject;
import com.binghai.pi.enums.RuleResult;
import com.binghai.pi.rules.context.OrderOnContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author huaishuo
 * @date 2019/7/28 下午3:45
 **/
@Component
public class OrderOn extends BaseRelayRule<OrderOnContext> {

    @Override
    public RuleResult evaluate(OrderOnContext context) {
        if (runnable(context)) {
            return RuleResult.ON;
        }
        return null;
    }

    @Override
    public String name() {
        return "预约开";
    }

    @Override
    public String contextName() {
        return OrderOnContext.class.getSimpleName();
    }

    @Override
    public boolean valid(OrderOnContext context) {
        return now() - buffer < context.getTs();
    }

    @Override
    public boolean runnable(OrderOnContext context) {
        return now() > context.getTs() && now() - buffer < context.getTs();
    }

    @Override
    public OrderOnContext create(Map map) {
        return new JSONObject(map).toJavaObject(OrderOnContext.class);
    }

}

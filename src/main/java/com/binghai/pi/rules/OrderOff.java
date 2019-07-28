package com.binghai.pi.rules;

import com.alibaba.fastjson.JSONObject;
import com.binghai.pi.enums.RuleResult;
import com.binghai.pi.rules.context.OrderOffContext;
import com.binghai.pi.utils.TimeTools;
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
        String now = TimeTools.piFormat(now());
        if (context.getRepeat()) {
            if (now.substring(4).equals(context.getTime().substring(4))) {
                return RuleResult.OFF;
            }
        } else {
            if (now.equals(context.getTime())) {
                return RuleResult.OFF;
            }
        }
        return null;
    }

    @Override
    public String name() {
        return "预约关";
    }

    @Override
    public String contextName() {
        return OrderOff.class.getSimpleName();
    }

    @Override
    public boolean valid(OrderOffContext context) {
        if (context.getRepeat()) {
            return true;
        }
        return now() < TimeTools.piParse(context.getTime());
    }

    @Override
    public OrderOffContext create(Map map) {
        return new JSONObject(map).toJavaObject(OrderOffContext.class);
    }
}

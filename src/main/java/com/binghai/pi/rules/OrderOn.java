package com.binghai.pi.rules;

import com.alibaba.fastjson.JSONObject;
import com.binghai.pi.enums.RuleResult;
import com.binghai.pi.rules.context.OrderOnContext;
import com.binghai.pi.utils.TimeTools;
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
        String now = TimeTools.piFormat(now());
        if (context.getRepeat()) {
            if (now.substring(4).equals(context.getTime().substring(4))) {
                return RuleResult.ON;
            }
        } else {
            if (now.equals(context.getTime())) {
                return RuleResult.ON;
            }
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
        if (context.getRepeat()) {
            return true;
        }
        return now() - buffer < TimeTools.piParse(context.getTime());
    }

    @Override
    public OrderOnContext create(Map map) {
        return new JSONObject(map).toJavaObject(OrderOnContext.class);
    }

}

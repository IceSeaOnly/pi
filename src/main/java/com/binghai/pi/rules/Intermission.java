package com.binghai.pi.rules;

import com.alibaba.fastjson.JSONObject;
import com.binghai.pi.enums.RuleResult;
import com.binghai.pi.rules.context.IntermissionContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author huaishuo
 * @date 2019/7/28 下午4:08
 **/
@Component
public class Intermission extends BaseRelayRule<IntermissionContext> {
    @Override
    public RuleResult evaluate(IntermissionContext context) {
        long start = context.getStartS();
        long dis = now() / 1000 - start;
        return dis % context.getInvalidTs() == 0 ? RuleResult.FLIP : null;
    }

    @Override
    public String name() {
        return "间歇翻转电门";
    }

    @Override
    public String contextName() {
        return IntermissionContext.class.getSimpleName();
    }

    @Override
    public boolean valid(IntermissionContext context) {
        return now() < context.getInvalidTs();
    }

    @Override
    public IntermissionContext create(Map map) {
        return new JSONObject(map).toJavaObject(IntermissionContext.class);
    }
}

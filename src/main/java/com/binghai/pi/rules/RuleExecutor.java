package com.binghai.pi.rules;

import com.binghai.pi.enums.RuleResult;
import com.binghai.pi.rules.context.RuleContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huaishuo
 * @date 2019/7/28 下午4:45
 **/
@Component
public class RuleExecutor {
    private Map<String, BaseRelayRule> executors = new HashMap<>();

    public RuleResult execute(RuleContext context) {
        return executors.get(context.getClass().getSimpleName()).evaluate(context);
    }

    public boolean isValid(RuleContext context) {
        return executors.get(context.getClass().getSimpleName()).valid(context);
    }

    public RuleContext createContext(String contextName, Map map) {
        return executors.get(contextName).create(map);
    }

    @Autowired
    public void ready(List<BaseRelayRule> relayRuleList) {
        relayRuleList.forEach(r -> executors.put(r.contextName(), r));
    }
}

package com.binghai.pi.rules;

import com.alibaba.fastjson.JSONObject;
import com.binghai.pi.enums.RuleResult;
import com.binghai.pi.rules.context.RuleContext;
import com.binghai.pi.utils.BaseBean;

import java.util.Map;

/**
 * @author huaishuo
 * @date 2019/7/28 下午3:42
 **/
public abstract class BaseRelayRule<T extends RuleContext> extends BaseBean {
    static final long buffer = 15 * 1000;

    public abstract RuleResult evaluate(T context);

    public abstract String name();

    public abstract String contextName();

    public abstract boolean valid(T context);

    public abstract T create(Map map);
}

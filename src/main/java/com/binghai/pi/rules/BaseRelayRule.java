package com.binghai.pi.rules;

import com.binghai.pi.enums.RuleResult;
import com.binghai.pi.rules.context.RuleContext;
import com.binghai.pi.utils.BaseBean;

import java.util.Map;

/**
 * @author huaishuo
 * @date 2019/7/28 下午3:42
 **/
public abstract class BaseRelayRule<T extends RuleContext> extends BaseBean {
    static final long buffer = 3 * 1000;

    public abstract RuleResult evaluate(T context);

    public abstract String name();

    public abstract String contextName();

    /*
    * 当前时刻任务是否已经过期
    * **/
    public abstract boolean valid(T context);
    /*
     * 当前时刻任务是否可执行
     * **/
    public abstract boolean runnable(T context);

    public abstract T create(Map map);
}

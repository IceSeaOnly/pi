package com.binghai.pi.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.binghai.pi.entity.Relay;
import com.binghai.pi.entity.RelayTask;
import com.binghai.pi.enums.RuleResult;
import com.binghai.pi.rules.RuleExecutor;
import com.binghai.pi.rules.context.RuleContext;
import com.binghai.pi.service.BaseService;
import com.binghai.pi.service.RelayService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author huaishuo
 * @date 2019/7/18 下午5:52
 **/
@Component
@EnableScheduling
@ConditionalOnProperty(prefix = "ice", name = "env", havingValue = "prod")
public class RelayServiceImpl extends BaseService<Relay> implements RelayService {

    @Override
    @Transactional
    public void flip(Long relayId) {
        Relay relay = findById(relayId);
        relay.flip();
        update(relay);
    }

    @Override
    public Relay get(Long relayId) {
        return findById(relayId);
    }

    @Override
    public List<Relay> list() {
        return findAll(99);
    }

    @Override
    @Transactional
    public void on(Long relayId) {
        Relay relay = findById(relayId);
        relay.to(relay.getOnStatus());
        update(relay);
    }

    @Override
    @Transactional
    public void off(Long relayId) {
        Relay relay = findById(relayId);
        relay.to(relay.getOffStatus());
        update(relay);
    }

}

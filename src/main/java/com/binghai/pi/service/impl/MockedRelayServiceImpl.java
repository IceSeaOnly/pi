package com.binghai.pi.service.impl;

import com.binghai.pi.entity.Relay;
import com.binghai.pi.enums.RelayState;
import com.binghai.pi.service.RelayService;
import com.binghai.pi.utils.BaseBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author huaishuo
 * @date 2019/7/21 下午4:56
 **/
@Component
@ConditionalOnProperty(prefix = "ice", name = "env", havingValue = "dev")
public class MockedRelayServiceImpl extends BaseBean implements RelayService {

    @Override
    public void flip(Long relayId) {
        logger.info("mock {} flip", relayId);
    }

    @Override
    public Relay get(Long relayId) {
        return mock();
    }

    @Override
    public List<Relay> list() {
        List<Relay> list = new ArrayList<>();
        list.add(mock());
        list.add(mock());
        list.add(mock());
        return list;
    }

    @Override
    public void on(Long relayId) {
        logger.info("mock {} on", relayId);
    }

    @Override
    public void off(Long relayId) {
        logger.info("mock {} off", relayId);
    }

    private Relay mock() {
        Relay relay = new Relay();
        relay.setId(System.currentTimeMillis());
        relay.setIoId(0);
        relay.setStatus(System.currentTimeMillis() % 2 == 0 ? RelayState.HIGH : RelayState.LOW);
        relay.setName(UUID.randomUUID().toString().substring(30));
        relay.setOffStatus(RelayState.LOW);
        relay.setOnStatus(RelayState.HIGH);
        return relay;
    }
}

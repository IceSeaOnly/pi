package com.binghai.pi.controller;

import com.alibaba.fastjson.JSONArray;
import com.binghai.pi.enums.RelayStatus;
import com.binghai.pi.pojo.Relay;
import com.binghai.pi.utils.KvStore;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author huaishuo
 * @date 2019/7/3 下午5:41
 **/
@RestController
@RequestMapping("switch")
public class SwitchController extends BaseController implements InitializingBean {
    private static final String RELAY_LIST_KEY = "RELAY_LIST_KEY";
    private Map<Integer, Relay> relays = new HashMap<>();

    @GetMapping("list")
    public Object list() {
        return success(relays.values());
    }

    @GetMapping("flip")
    public Object flip(@RequestParam Integer gpio) {
        relays.get(gpio).flip();
        updateRelayList();
        return success();
    }

    @GetMapping("add")
    public Object add(@RequestParam Integer gpio, @RequestParam String name, @RequestParam String status) {
        if (relays.get(gpio) != null) {
            return fail("GPIO USING.");
        }

        Relay relay = new Relay();
        relay.setIoId(gpio);
        relay.setName(name);
        relay.setStatus(RelayStatus.valueOf(status));

        relay.recovery();
        relays.put(relay.getIoId(), relay);
        updateRelayList();
        return success();
    }

    private void updateRelayList() {
        KvStore.putOrUpdate(RELAY_LIST_KEY, relays.values());
    }

    @GetMapping("delete")
    public Object delete(@RequestParam Integer gpio) {
        if (relays.get(gpio) == null) {
            return success();
        }

        relays.get(gpio).shutdown();
        relays.remove(gpio);
        updateRelayList();
        return success();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        JSONArray array = KvStore.getJsonArray(RELAY_LIST_KEY);
        if (CollectionUtils.isEmpty(array)) {
            return;
        }

        for (int i = 0; i < array.size(); i++) {
            Relay relay = array.getObject(i, Relay.class);
            relay.recovery();
            relays.put(relay.getIoId(), relay);
        }
    }
}

package com.binghai.pi.pojo;

import com.binghai.pi.enums.RelayState;
import com.binghai.pi.gpio.GpioService;
import lombok.Data;

/**
 * @author huaishuo
 * @date 2019/7/3 下午6:11
 **/
@Data
public class Relay {
    private String name;
    private RelayState status;
    private Integer ioId;

    public Relay(String name, RelayState status, Integer ioId) {
        this.name = name;
        this.status = status;
        this.ioId = ioId;
    }

    public Relay() {

    }

    public void recovery() {
        GpioService.setTo(ioId, status);
    }

    public void toLow() {
        status = RelayState.LOW;
        GpioService.setTo(ioId, RelayState.LOW);
    }

    public void toHigh() {
        status = RelayState.HIGH;
        GpioService.setTo(ioId, RelayState.HIGH);
    }

    public void flip() {
        status = (status == RelayState.HIGH ? RelayState.LOW : RelayState.HIGH);
        GpioService.flip(ioId);
    }

    public void shutdown() {
        GpioService.shutdown(ioId);
    }
}

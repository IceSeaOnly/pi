package com.binghai.pi.pojo;

import com.binghai.pi.enums.RelayStatus;
import com.binghai.pi.gpio.GpioService;
import lombok.Data;

/**
 * @author huaishuo
 * @date 2019/7/3 下午6:11
 **/
@Data
public class Relay {
    private String name;
    private RelayStatus status;
    private Integer ioId;

    public Relay(String name, RelayStatus status, Integer ioId) {
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
        status = RelayStatus.LOW;
        GpioService.setTo(ioId, RelayStatus.LOW);
    }

    public void toHigh() {
        status = RelayStatus.HIGH;
        GpioService.setTo(ioId, RelayStatus.HIGH);
    }

    public void flip() {
        status = (status == RelayStatus.HIGH ? RelayStatus.LOW : RelayStatus.HIGH);
        GpioService.flip(ioId);
    }

    public void shutdown() {
        GpioService.shutdown(ioId);
    }
}

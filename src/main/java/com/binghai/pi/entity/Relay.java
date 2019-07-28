package com.binghai.pi.entity;

import com.binghai.pi.enums.RelayState;
import com.binghai.pi.gpio.GpioService;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author huaishuo
 * @date 2019/7/3 下午6:11
 **/
@Data
@Entity
public class Relay extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private RelayState status;
    private Integer ioId;

    private RelayState onStatus;
    private RelayState offStatus;

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

    public void to(RelayState st) {
        status = st;
        GpioService.setTo(ioId, st);
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
}

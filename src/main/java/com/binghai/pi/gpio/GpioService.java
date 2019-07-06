package com.binghai.pi.gpio;

import com.binghai.pi.enums.RelayStatus;
import com.pi4j.io.gpio.*;

/**
 * @author huaishuo
 * @date 2019/7/4 上午10:18
 **/

public class GpioService {
    private static GpioController gpio;

    static {
        gpio = GpioFactory.getInstance();
    }

    public static void setTo(Integer ioId, RelayStatus status) {
        System.out.println(ioId + " turn to " + status.name());
        GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(RaspiPin.getPinByAddress(ioId));
        if(status == RelayStatus.HIGH){
            pin.high();
        }else{
            pin.low();
        }

    }

    public static void flip(Integer ioId) {
        GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(RaspiPin.getPinByAddress(ioId));
        pin.toggle();
    }

    public static void sfhutdown(Integer ioId) {
        gpio.shutdown();
    }
}

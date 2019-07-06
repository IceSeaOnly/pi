package com.binghai.pi.gpio;



import com.binghai.pi.enums.RelayState;
import com.pi4j.io.gpio.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author huaishuo
 * @date 2019/7/4 上午10:18
 **/

public class GpioService {
    private static GpioController gpio;
    private static Map<Integer,GpioPinDigitalOutput> pins = new HashMap<>();

    static {
        gpio = GpioFactory.getInstance();
    }

    public static void setTo(Integer ioId, RelayState status) {
        GpioPinDigitalOutput io = getPin(ioId);
        if(status == RelayState.HIGH){
            io.high();
        }else{
            io.low();
        }
        System.out.println(ioId + " turn to " + status.name());
    }

    private static GpioPinDigitalOutput getPin(Integer io){
        if(pins.get(io) == null){
            GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(RaspiPin.getPinByAddress(io));
            pins.put(io,pin);
        }
        return pins.get(io);
    }

    public static void flip(Integer ioId) {
        GpioPinDigitalOutput io = getPin(ioId);
        io.toggle();
    }

    public static void shutdown(Integer ioId) {

    }
}

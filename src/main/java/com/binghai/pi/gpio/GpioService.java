package com.binghai.pi.gpio;

import com.binghai.pi.enums.RelayStatus;

/**
 * @author huaishuo
 * @date 2019/7/4 上午10:18
 **/

public class GpioService {

    public static void setTo(Integer ioId, RelayStatus status) {
        System.out.println(ioId + " turn to " + status.name());
    }

    public static void flip(Integer ioId) {
        System.out.println(ioId + " filp");
    }

    public static void shutdown(Integer ioId) {
        System.out.println(ioId + " shutdown");
    }
}

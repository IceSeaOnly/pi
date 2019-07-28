package com.binghai.pi.service;

import com.binghai.pi.entity.Relay;

import java.util.List;

/**
 * @author huaishuo
 * @date 2019/7/21 下午4:54
 **/
public interface RelayService {
    void flip(Long relayId);

    Relay get(Long relayId);

    List<Relay> list();

    void on(Long relayId);

    void off(Long relayId);
}

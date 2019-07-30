package com.binghai.pi.task;

import com.binghai.pi.dingtalk.FeedCardMessage;
import com.binghai.pi.dingtalk.FeedCardMessageItem;
import com.binghai.pi.utils.DingTalkService;
import com.binghai.pi.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author huaishuo
 * @date 2019/7/18 下午3:05
 **/
@Component
@EnableScheduling
public class IpPusher {
    private static final String LOCAL = "127.0.0.1";

    @Value("${server.port}")
    private String port;
    private String lastScanResult = "LAST";
    @Autowired
    private DingTalkService dingTalkService;

    @Scheduled(cron = "0/5 * * * * ?")
    public void run() throws Exception {
        String ip = HttpUtils.localIp();
        if (LOCAL.equals(ip)) {
            System.out.println("waiting internet connect...");
        }

        if (!lastScanResult.equals(ip)) {
            lastScanResult = ip;
            pushLocal(ip);
        }
    }

    private void pushLocal(String ip) throws Exception {
        String url = "http://" + ip + ":" + port + "/switch/list";
        System.out.println(url);
        String qrCodeUrl = "https://wx.nanayun.cn/qrCode?t=" + url;
        FeedCardMessage msg = new FeedCardMessage();
        List<FeedCardMessageItem> items = new ArrayList<>();

        FeedCardMessageItem head = new FeedCardMessageItem();
        head.setTitle("SMART BOX NOW ONLINE");
        head.setPicURL("http://cdn.binghai.site/o_1dg20ea7oab8117m6561m601g56a.jpg");
        head.setMessageURL(url);

        FeedCardMessageItem redirect = new FeedCardMessageItem();
        redirect.setTitle("go to the control page");
        redirect.setPicURL("http://cdn.binghai.site/o_1dg20ph0a5di11f01kmrbgp11kua.jpg");
        redirect.setMessageURL(url);

        FeedCardMessageItem qrUrl = new FeedCardMessageItem();
        qrUrl.setTitle("run on [" + ip + "]");
        qrUrl.setPicURL(qrCodeUrl);
        qrUrl.setMessageURL(qrCodeUrl);

        items.add(head);
        items.add(redirect);
        items.add(qrUrl);
        msg.setFeedItems(items);
        dingTalkService.send(msg);
    }
}

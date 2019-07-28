package com.binghai.pi.utils;

import com.alibaba.fastjson.JSONObject;
import com.binghai.pi.dingtalk.Message;
import com.binghai.pi.dingtalk.SendResult;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author huaishuo
 * @date 2019/1/19 下午1:22
 **/
@Service
public class DingTalkService {
    HttpClient httpclient = HttpClients.createDefault();

    private static final String webhook
        = "https://oapi.dingtalk"
        + ".com/robot/send?access_token=b4d102d2b8aa2862148edeca0af2122c8d0760f16fdf7aac1a623e456e60f698";

    public SendResult send(Message message) throws IOException {

        HttpPost httppost = new HttpPost(webhook);
        httppost.addHeader("Content-Type", "application/json; charset=utf-8");
        StringEntity se = new StringEntity(message.toJsonString(), "utf-8");
        httppost.setEntity(se);

        SendResult sendResult = new SendResult();
        HttpResponse response = httpclient.execute(httppost);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String result = EntityUtils.toString(response.getEntity());
            JSONObject obj = JSONObject.parseObject(result);

            Integer errcode = obj.getInteger("errcode");
            sendResult.setErrorCode(errcode);
            sendResult.setErrorMsg(obj.getString("errmsg"));
            sendResult.setIsSuccess(errcode.equals(0));
        }

        return sendResult;
    }
}

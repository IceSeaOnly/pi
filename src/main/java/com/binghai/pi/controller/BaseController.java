package com.binghai.pi.controller;

import com.alibaba.fastjson.JSONObject;
import com.binghai.pi.utils.BaseBean;

/**
 * @author huaishuo
 * @date 2019/7/4 下午2:19
 **/
public abstract class BaseController extends BaseBean {

    public JSONObject success() {
        return success(null);
    }

    public JSONObject success(Object obj) {
        JSONObject ret = new JSONObject();
        ret.put("code", "0000");
        ret.put("data", obj);
        return ret;
    }

    public JSONObject fail(String msg) {
        JSONObject ret = new JSONObject();
        ret.put("code", "1000");
        ret.put("err", msg);
        return ret;
    }
}

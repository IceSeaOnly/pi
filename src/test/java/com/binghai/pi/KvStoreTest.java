package com.binghai.pi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.binghai.pi.utils.KvStore;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author huaishuo
 * @date 2019/7/3 下午5:33
 **/
public class KvStoreTest {
    @Test
    public void test() {
        KvStore.putOrUpdate("hello", "world");
        Assert.assertEquals("world", KvStore.getString("hello"));
        KvStore.putOrUpdate("hello", "world hi");
        Assert.assertEquals("world hi", KvStore.getString("hello"));

        KvStore.putOrUpdate("number", 123456);
        Assert.assertTrue(123456 == KvStore.getInteger("number"));

        JSONObject obj = new JSONObject();
        obj.put("good", "luck");

        KvStore.putOrUpdate("json", obj);
        Assert.assertTrue(KvStore.getJsonObject("json").getString("good").equals("luck"));

        JSONArray arr = new JSONArray();
        arr.add(obj);

        KvStore.putOrUpdate("arr", arr);
        Assert.assertTrue(KvStore.getJsonArray("arr").size() == 1);

        KvStore.delete("arr");
        Assert.assertTrue(KvStore.getJsonArray("arr") == null);
    }
}

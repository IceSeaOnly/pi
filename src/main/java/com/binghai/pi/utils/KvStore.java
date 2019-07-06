package com.binghai.pi.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.File;

/**
 * @author huaishuo
 * @date 2019/7/3 下午5:16
 **/
public class KvStore {
    private static String STORE_PATH = "/tmp/kv_store/pi/";

    static {
        File dir = new File(STORE_PATH);
        dir.mkdirs();
    }

    public static void putOrUpdate(String key, Object object) {
        FileLocation location = parseFileLocation(key);
        File dir = new File(location.dir);
        dir.mkdirs();
        IoUtils.WriteCH(location.file, JSON.toJSONString(object));
    }

    public static String getString(String key) {
        FileLocation location = parseFileLocation(key);
        File file = new File(location.file);
        return file.exists() ? IoUtils.ReadCH(location.file) : null;
    }

    public static void delete(String key) {
        FileLocation location = parseFileLocation(key);
        File file = new File(location.file);
        if (file.exists()) {
            file.delete();
        }
    }

    public static Integer getInteger(String key) {
        String r = getString(key);
        return r == null ? null : Integer.valueOf(r);
    }

    public static JSONObject getJsonObject(String key) {
        String r = getString(key);
        return r == null ? null : JSONObject.parseObject(r);
    }

    public static JSONArray getJsonArray(String key) {
        String r = getString(key);
        return r == null ? null : JSONArray.parseArray(r);
    }

    public static <T> T getObject(String key, Class<T> clazz) {
        String r = getString(key);
        return r == null ? null : JSON.parseObject(r, clazz);
    }

    private static FileLocation parseFileLocation(String ori) {
        String md5 = MD5.encryption(ori);
        int total = 0;
        for (int i = 0; i < md5.length(); i++) {
            total += md5.charAt(i) - '0';
        }
        FileLocation location = new FileLocation();
        location.dir = STORE_PATH + total + "/";
        location.file = location.dir + md5;
        return location;
    }

    static class FileLocation {
        private String dir;
        private String file;
    }
}

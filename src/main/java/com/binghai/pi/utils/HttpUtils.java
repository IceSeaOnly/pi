package com.binghai.pi.utils;

/**
 * Created by Administrator on 2017/1/30.
 */

import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.net.*;
import java.util.Enumeration;
import java.util.UUID;

public class HttpUtils {

    private static String sendGet(String url, String param) {
        String urlNameString = param == null ? url : url + "?" + param;

        String result = "";
        BufferedReader in = null;
        try {
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            connection.setReadTimeout(3000);
            connection.setRequestProperty("Content-type", "application/json");
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                connection.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            //            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    private static String sendPost(String url, String param, String body, String contentType) {

        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(param == null ? url : url + "?" + param);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            conn.setReadTimeout(1000);
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("Content-Type", contentType);
            conn.setRequestProperty("Accept-Language", "Keep-Alive");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 "
                    + "Safari/537.36");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(body);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            //            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static String sendPostForImageFile(String url, String param, String body, String contentType) {
        PrintWriter out = null;
        BufferedReader in = null;
        try {
            URL realUrl = new URL(param == null ? url : url + "?" + param);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            conn.setReadTimeout(1000);
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("Content-Type", contentType);
            conn.setRequestProperty("Accept-Language", "Keep-Alive");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 "
                    + "Safari/537.36");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(body);
            // flush输出流的缓冲
            out.flush();

            if (conn.getContentType().contains("image")) {
                //开始获取数据
                String filePath = "/tmp/" + UUID.randomUUID().toString() + ".png";
                BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                OutputStream os = new FileOutputStream(new File(filePath));
                int len;
                byte[] arr = new byte[1024];
                while ((len = bis.read(arr)) != -1) {
                    os.write(arr, 0, len);
                    os.flush();
                }
                os.close();
                return filePath;
            } else {
                // 定义BufferedReader输入流来读取URL的响应
                in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"));
                String line;
                while ((line = in.readLine()) != null) {
                    System.out.println(line);
                }
                throw new RuntimeException("response is not a image file.");
            }

        } catch (Exception e) {
            System.out.println("发送 Post4File 请求出现异常！" + e);
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public static JSONObject sendJSONGet(String url, String param) {
        String res = sendGet(url, param);
        return JSONObject.parseObject(res);
    }

    public static JSONObject sendJSONPost(String url, String param, String body) {
        String res = sendPost(url, param, body, "application/json;");
        return JSONObject.parseObject(res);
    }

    public static String localIp() {
        try {
            InetAddress address = InetAddress.getLocalHost();
            if (address.isLoopbackAddress()) {
                Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
                while (allNetInterfaces.hasMoreElements()) {
                    NetworkInterface netInterface = allNetInterfaces.nextElement();
                    Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        InetAddress ip = addresses.nextElement();
                        if (!ip.isLinkLocalAddress() && !ip.isLoopbackAddress() && ip instanceof Inet4Address) {
                            return ip.getHostAddress();
                        }
                    }
                }
            }
            return address.getHostAddress();
        } catch (Exception e) {
            return null;
        }
    }

}

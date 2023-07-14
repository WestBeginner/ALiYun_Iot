package com.aliyun.iot;


import com.alibaba.fastjson.JSON;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.iot.model.v20180120.QueryDevicePropertyStatusRequest;
import com.aliyuncs.iot.model.v20180120.QueryDevicePropertyStatusResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Properties;

public class MainClass {

    public static DefaultAcsClient client;
    private static String  accessKeyId = "LTAI5tJm**************";
    private static String  secret = "2PR1crkp**************";
    private static String  regionId = "cn-shanghai";

    private static String deviceProductkey="j0u*******";
    private static String deviceName="My_******";

    private static String iotid="iot-06******";

    public static void main(String[] args) {

        IClientProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, secret);
        // 初始化client
        client = new DefaultAcsClient(profile); //初始化SDK客户端


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true)
                {
                    getValue();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();

    }

    private static void getValue() {
        // 构造查询设备属性请求
        QueryDevicePropertyStatusRequest request = new QueryDevicePropertyStatusRequest();
        request.setRegionId(regionId);
        request.setIotInstanceId(iotid);
        request.setDeviceName(deviceName);
        request.setProductKey(deviceProductkey);
        try {
            // 发起查询设备属性请求
            QueryDevicePropertyStatusResponse response = client.getAcsResponse(request);
            // 处理查询结果
            // 可以从response中获取传感器的数据
            // 获取传感器数据
            String string=JSON.toJSONString(response);
            // 解析JSON数据
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = (JsonObject) parser.parse(string);
            // 获取数据字段
            JsonObject data = (JsonObject) jsonObject.get("data");
            JsonArray list = (JsonArray) data.get("list");
            //将传感器数据输出
            System.out.println("传感器数据：" + list.get(0).getAsJsonObject().get("value"));
        } catch (ClientException e) {
            // 处理异常
            e.printStackTrace();
        }
    }
}

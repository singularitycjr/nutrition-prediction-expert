package com.tongji.service.service;

import com.alibaba.fastjson.JSON;
import com.tongji.common.utils.HttpClientUtil;
import cn.hutool.json.JSONObject;
import com.tongji.model.json.LapDepthJSON;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class AaaaTest {

    public static void main(String[] args) throws Exception {
        System.out.println("Hello World!");

        Map<String, String> map = new HashMap<>();
        map.put("appid", "1111");

        JSONObject food1 = new JSONObject();
        food1.put("num", 38);
        food1.put("name", "海参");

        JSONObject food2 = new JSONObject();
        food2.put("num", 135);
        food2.put("name", "荞麦馒头");

        JSONObject food3 = new JSONObject();
        food3.put("num", 203);
        food3.put("name", "鸡蛋");

        List<JSONObject> foods = List.of(food1, food2, food3);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("foods", foods);

        String json = HttpClientUtil.doPostJson("http://100.80.163.197:8997/predict", jsonObject);
        log.info("json: {}", json);
        LapDepthJSON lapDepthJSON = JSON.parseObject(json, LapDepthJSON.class);
        log.info("lapDepthJSON: {}", lapDepthJSON);
    }
}

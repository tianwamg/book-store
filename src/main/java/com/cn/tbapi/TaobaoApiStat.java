package com.cn.tbapi;

import com.cn.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TaobaoApiStat {

    @Autowired
    RedisTemplate redisTemplate;

    @Async
    public void sendApiStat(){
        redisTemplate.opsForValue().increment("taobao_stat");
        String url = "http://top.cnedo.com/ApiService/AddLog";
        Map<String,String> map = new HashMap<>();
        map.put("appKey","21310116");
        map.put("method","taobao.vas.subscribe.get");
        map.put("name","jdhstore");
        map.put("isFail","0");
        Utils.doHttpRequest(url,map);
    }
}

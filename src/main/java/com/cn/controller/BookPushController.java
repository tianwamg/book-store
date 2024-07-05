package com.cn.controller;

import com.cn.request.CommonRequest;
import com.cn.response.ResultResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 图书推送
 */
@RestController
@RequestMapping("/book/push")
public class BookPushController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    public ResultResponse push(CommonRequest request){
        //获取推送信息，拿取缓存session，推送至mq
        String sessionKey = redisTemplate.opsForValue().get("taobao_"+request.getUserId()).toString();
        return null;
    }

    public ResultResponse down(){
        return null;
    }

    public ResultResponse up(){
        return null;
    }
}

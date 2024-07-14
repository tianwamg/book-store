package com.cn.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.cn.domain.BookInfo;
import com.cn.dto.PushTaskDto;
import com.cn.request.CommonRequest;
import com.cn.response.ResultResponse;
import com.cn.service.IBookInfoService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @Autowired
    IBookInfoService iBookInfoService;

    @PostMapping("/send/task")
    public ResultResponse push(@RequestBody CommonRequest<PushTaskDto> request){
        //获取推送信息，拿取缓存session，推送至mq
        PushTaskDto pushTaskDto = request.getRequestData();
        pushTaskDto.setUserId(request.getUserId());
        BookInfo bookInfo = new BookInfo();
        bookInfo.setUserId(request.getUserId());
        bookInfo.setStatus(1);
        int count = iBookInfoService.count(bookInfo);
        pushTaskDto.setPushNum(pushTaskDto.getPushNum()<count? pushTaskDto.getPushNum() : count);
        rabbitTemplate.convertAndSend("pushbook", JSON.toJSONString(pushTaskDto));
        return ResultResponse.success("书籍进入发布任务队列，即将开始...");
    }

    public ResultResponse down(){
        return null;
    }

    public ResultResponse up(){
        return null;
    }
}

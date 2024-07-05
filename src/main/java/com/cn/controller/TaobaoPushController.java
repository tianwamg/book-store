package com.cn.controller;

import com.cn.dto.PushTaskDto;
import com.cn.request.CommonRequest;
import com.cn.response.ResultResponse;
import com.cn.tbapi.SingletonClient;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * 淘宝商品管理
 */
@RestController
@RequestMapping("sys/taobao")
public class TaobaoPushController {

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 保存用户session
     * @return
     */
    public ResultResponse saveSessionKey(CommonRequest request){
        redisTemplate.opsForValue().set("taobao_"+request.getUserId(),request.getRequestData().toString(),7, TimeUnit.DAYS);
        return ResultResponse.success("true");
    }

    /**
     * 获取用户session
     * @return
     */
    public ResultResponse<String> getSessionKey(CommonRequest request){
        String sessionKey = redisTemplate.opsForValue().get("taobao_"+request.getUserId()).toString();
        return ResultResponse.success(sessionKey);
    }


    /**
     * 获取分类模版
     * @return
     */
    public ResultResponse getCatTemplate(CommonRequest request){
        TaobaoClient client = SingletonClient.INSTANCE.getClient();
        return null;
    }

    /**
     * 获取用户自定义模版
     */
    public ResultResponse getUserTemplate(CommonRequest request){
        TaobaoClient client = SingletonClient.INSTANCE.getClient();
        return null;
    }

    /**
     * 获取店铺运费模版
     * @return
     */
    public ResultResponse getPostFeeTemplate(CommonRequest request){
        TaobaoClient client = SingletonClient.INSTANCE.getClient();
        return null;
    }
}

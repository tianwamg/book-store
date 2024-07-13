package com.cn.controller;

import com.cn.dto.PushTaskDto;
import com.cn.dto.TaobaoCatDto;
import com.cn.request.CommonRequest;
import com.cn.response.ResultResponse;
import com.cn.tbapi.SingletonClient;
import com.cn.tbapi.TaobaoService;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.DeliveryTemplate;
import com.taobao.api.domain.ItemCat;
import com.taobao.api.domain.SellerCat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 淘宝商品管理
 */
@RestController
@RequestMapping("/sys/taobao")
public class TaobaoPushController {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    TaobaoService taobaoService;

    /**
     * 保存用户session
     * @return
     */
    public ResultResponse saveSessionKey(@RequestBody CommonRequest request){
        redisTemplate.opsForValue().set("taobao_"+request.getUserId(),request.getRequestData().toString(),7, TimeUnit.DAYS);
        return ResultResponse.success("true");
    }

    /**
     * 获取用户session
     * @return
     */
    public ResultResponse<String> getSessionKey(@RequestBody CommonRequest request){
        String sessionKey = redisTemplate.opsForValue().get("taobao_"+request.getUserId()).toString();
        return ResultResponse.success(sessionKey);
    }


    /**
     * 获取分类模版
     * @return
     */
    @PostMapping("/temp/cat")
    public ResultResponse<List<ItemCat>> getCatTemplate(@RequestBody CommonRequest request){

        return ResultResponse.success(taobaoService.getCatTemplate());

    }

    /**
     * 获取用户自定义模版
     */
    @PostMapping("/seller/cat")
    public ResultResponse<List<SellerCat>> getCatSerller(@RequestBody CommonRequest request){
        return ResultResponse.success(taobaoService.getSellerCat(request.getUserId()));
    }

    /**
     * 获取店铺运费模版
     * @return
     */
    @PostMapping("/temp/fee")
    public ResultResponse<List<DeliveryTemplate>> getPostFeeTemplate(@RequestBody CommonRequest request){
        return ResultResponse.success(taobaoService.getPostFeeTemplate(request.getUserId()));
    }

    @PostMapping("/subcat")
    public ResultResponse<List<TaobaoCatDto>> getSubCat(){
        return ResultResponse.success(taobaoService.getCats());
    }
}

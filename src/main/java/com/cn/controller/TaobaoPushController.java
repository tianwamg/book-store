package com.cn.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.cn.anno.PassToken;
import com.cn.domain.SysUser;
import com.cn.dto.PushTaskDto;
import com.cn.dto.TaobaoCatDto;
import com.cn.dto.TaobaoTokenDto;
import com.cn.request.CommonRequest;
import com.cn.response.ResultResponse;
import com.cn.service.ISysUerService;
import com.cn.tbapi.SingletonClient;
import com.cn.tbapi.TaobaoService;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.DeliveryTemplate;
import com.taobao.api.domain.ItemCat;
import com.taobao.api.domain.SellerCat;
import com.taobao.api.domain.Shop;
import com.taobao.api.request.AlibabaItemPublishSchemaGetRequest;
import com.taobao.api.response.AlibabaItemPublishSchemaGetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
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

    @Autowired
    ISysUerService iSysUerService;

    /**
     * 保存用户session
     * @return
     */
    @PostMapping("/savekey")
    public ResultResponse saveSessionKey(@RequestBody CommonRequest<String> request){
        //首先查询当前账号绑定店铺是否一致
        Shop shop = taobaoService.getSellerInfo(request.getRequestData());
        System.out.println("shop info..."+JSON.toJSONString(shop));
        SysUser sysUser = iSysUerService.getById(request.getUserId());
        redisTemplate.opsForValue().set("taobao_key_"+request.getUserId(),request.getRequestData(),7, TimeUnit.DAYS);
        return ResultResponse.success("true");
    }

    /**
     * 获取用户session
     * @return
     */
    @PostMapping("/getkey")
    public ResultResponse<String> getSessionKey(@RequestBody CommonRequest request){
        String sessionKey = (String) redisTemplate.opsForValue().get("taobao_key_"+request.getUserId());
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

    @PassToken
    @PostMapping("/schema")
    public void schema() throws ApiException {
        TaobaoClient client = SingletonClient.INSTANCE.getClient();
        AlibabaItemPublishSchemaGetRequest req = new AlibabaItemPublishSchemaGetRequest();
        req.setImages("https://img.alicdn.com/imgextra/i1/2506614820/O1CN01GrtC7i1lTc0aq9yzf_!!2506614820.jpg");
        req.setItemType("b");
        req.setBizType("taobao");
        req.setMarket("taobao");
        req.setCatId(50010485l);
        //req.setSpuId(32323L);
        //req.setBarcode("6932529211107");
        AlibabaItemPublishSchemaGetResponse rsp = client.execute(req, "6101a02cc21b745ZZ151123d6051766561516d40159ef3a3307647498");
        System.out.println(rsp.getBody());
    }

    @PassToken
    @GetMapping("/taobaotoken")
    public ResultResponse<String> getTaobaoToken(@RequestParam Map<String,Object> map){
        System.out.println(JSON.toJSONString(map));
        //TaobaoTokenDto tokenDto = JSONObject.parseObject(map.toString(),TaobaoTokenDto.class);
       // redisTemplate.opsForValue().set("taobao_token_"+taobaoTokenDto.getShopNick(), JSONObject.toJSONString(taobaoTokenDto));
        return ResultResponse.success(JSON.toJSONString(map));

    }
}

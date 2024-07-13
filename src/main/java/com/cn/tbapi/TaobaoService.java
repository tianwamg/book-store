package com.cn.tbapi;

import com.alibaba.fastjson2.JSONArray;
import com.cn.dto.TaobaoCatDto;
import com.cn.request.CommonRequest;
import com.cn.response.ResultResponse;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.FileItem;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.DeliveryTemplate;
import com.taobao.api.domain.ItemCat;
import com.taobao.api.domain.SellerCat;
import com.taobao.api.request.DeliveryTemplatesGetRequest;
import com.taobao.api.request.ItemcatsGetRequest;
import com.taobao.api.request.PictureUploadRequest;
import com.taobao.api.request.SellercatsListGetRequest;
import com.taobao.api.response.DeliveryTemplatesGetResponse;
import com.taobao.api.response.ItemcatsGetResponse;
import com.taobao.api.response.PictureUploadResponse;
import com.taobao.api.response.SellercatsListGetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Cacheable(cacheNames = "apitb")
@Service
public class TaobaoService {


    @Autowired
    RedisTemplate redisTemplate;


    /**
     * 获取分类模版
     * @return
     */
    @Cacheable(cacheNames = "itemcat")
    public List<ItemCat> getCatTemplate(){
        TaobaoClient client = SingletonClient.INSTANCE.getClient();
        ItemcatsGetRequest req = new ItemcatsGetRequest();
        req.setFields("cid,parent_cid,name,is_parent");
        req.setParentCid(33l);
        ItemcatsGetResponse rsp = null;
        try {
            rsp = client.execute(req);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return rsp.getItemCats();
    }

    /**
     * 获取用户自定义模版
     */
    @Cacheable(cacheNames = "sellercat",key = "#userId")
    public List<SellerCat> getSellerCat(Long userId){
        String sessionKey = redisTemplate.opsForValue().get("taobao_"+userId).toString();
        TaobaoClient client = SingletonClient.INSTANCE.getClient();
        SellercatsListGetRequest req = new SellercatsListGetRequest();
        req.setFields("cid,name");
        SellercatsListGetResponse rsp = null;
        try {
            rsp = client.execute(req, sessionKey);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return rsp.getSellerCats();
    }

    /**
     * 获取店铺运费模版
     * @return
     */
    @Cacheable(cacheNames = "postfee",key = "#userId")
    public List<DeliveryTemplate> getPostFeeTemplate(Long userId){
        String sessionKey = redisTemplate.opsForValue().get("taobao_"+userId).toString();
        TaobaoClient client = SingletonClient.INSTANCE.getClient();
        DeliveryTemplatesGetRequest req = new DeliveryTemplatesGetRequest();
        req.setFields("template_id,template_name,created,modified,supports,assumer,valuation,query_express,query_ems,query_cod,query_post");
        DeliveryTemplatesGetResponse rsp = null;
        try {
            rsp = client.execute(req, sessionKey);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return rsp.getDeliveryTemplates();
    }

    public List<TaobaoCatDto> getCats(){
        String cat = redisTemplate.opsForValue().get("tao_sub_cat").toString();
        return JSONArray.parseArray(cat,TaobaoCatDto.class);
    }




}

package com.cn.tbapi;

import com.alibaba.fastjson2.JSONObject;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.DeliveryTemplate;
import com.taobao.api.domain.ItemCat;
import com.taobao.api.domain.SellerCat;
import com.taobao.api.request.*;
import com.taobao.api.response.*;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 淘宝商品发布
 */
public class GoodsPush {

    private static final String appkey = "13122675465";
    private static final String secret = "0fb5104fd82969b7d591694104422bf8";
    private static final String url = "http://39.98.70.213:30002/top/rest2";


    /**
     * 获取二级分类类目模板
     * https://open.taobao.com/v2/doc#/apiFile?docType=2&docId=122
     * @return
     */
    public List<ItemCat> getCatTemplate(){
        TaobaoClient client = new DefaultTaobaoClient(url,appkey,secret);
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
     * 获取淘宝店铺卖家自定义分类
     * https://open.taobao.com/v2/doc#/apiFile?docType=2&docId=65
     */
    public List<SellerCat> getSellerCats(){
        TaobaoClient client = new DefaultTaobaoClient(url,appkey,secret);
        SellercatsListGetRequest req = new SellercatsListGetRequest();
        req.setFields("cid,name");
        SellercatsListGetResponse rsp = null;
        try {
            rsp = client.execute(req, "610181057e897b8ba317eb84a9ac7c1a88e074b18fd13bd2506614820");
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return rsp.getSellerCats();

    }

    /**
     * 获取卖家运费模版
     * https://open.taobao.com/v2/doc#/apiFile?docType=2&docId=10916
     * @return
     */
    public List<DeliveryTemplate> getPostFee(){
        TaobaoClient client = new DefaultTaobaoClient(url,appkey,secret);
        DeliveryTemplatesGetRequest req = new DeliveryTemplatesGetRequest();
        req.setFields("template_id,template_name,created,modified,supports,assumer,valuation,query_express,query_ems,query_cod,query_post");
        DeliveryTemplatesGetResponse rsp = null;
        try {
            rsp = client.execute(req, "6100402dfe1fb0bc8c2eb17198ec27fb71e78eee49336da2506614820");
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return rsp.getDeliveryTemplates();
    }

    /**
     * 商品发布
     * TODO 多线程发布
     * https://open.taobao.com/v2/doc?spm=4661s.15212433.0.0.2b3c669amDK5Qc#/apiFile?docType=2&docId=53962
     */
    public void taoGoodsPush(){
        TaobaoClient taobaoClient = new DefaultTaobaoClient(url,appkey,secret);
        AlibabaItemPublishSubmitRequest req = new AlibabaItemPublishSubmitRequest();
        req.setBizType("taobao");
        req.setMarket("taobao");
        req.setCatId(1698370539l);
        //req.setSpuId(32323L);
        //req.setBarcode("534523452345");
        req.setSchema("<itemSchema></itemSchema>");
        AlibabaItemPublishSubmitResponse rsp = null;
        try {
            rsp = taobaoClient.execute(req, "sessionKey");
        } catch (ApiException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = JSONObject.parseObject(rsp.getBody());
        String code = jsonObject.getString("code");
        //jsonObject.getLong("item_id")
        if(!StringUtils.isEmpty(code) && !code.equals("200")){//失败转存
            jsonObject.getString("sub_msg");
        }else {
            //获取淘宝商品id，上传图片
            ItemImgUploadRequest imgReq = new ItemImgUploadRequest();
            imgReq.setNumIid(jsonObject.getLong("item_id"));
            //imgReq.setId(12345L);
            imgReq.setPosition(1L);
            imgReq.setImage(null);
            imgReq.setIsMajor(true);
            imgReq.setIsRectangle(false);
            try {
                ItemImgUploadResponse imgRsp = taobaoClient.execute(imgReq, "sessionKey");
            } catch (ApiException e) {
                e.printStackTrace();
            }
            System.out.println(rsp.getBody());
        }
    }

    /**
     * 淘宝商品上架
     * https://open.taobao.com/v2/doc?spm=4661s.15212433.0.0.2b3c669amDK5Qc#/apiFile?docType=2&docId=32
     */
    public void uplist() throws ApiException {
        TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
        ItemUpdateListingRequest itemReq = new ItemUpdateListingRequest();
        itemReq.setNumIid(1000231L);
        itemReq.setNum(2L);
        ItemUpdateListingResponse itemRsp = client.execute(itemReq, "sessionKey");
        System.out.println(itemRsp.getBody());
    }

    /**
     * 商品下架
     */
    public void offlist() throws ApiException {
        TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
        ItemUpdateDelistingRequest req = new ItemUpdateDelistingRequest();
        req.setNumIid(1000231L);
        ItemUpdateDelistingResponse rsp = client.execute(req, "sessionKey");
        System.out.println(rsp.getBody());
    }


}

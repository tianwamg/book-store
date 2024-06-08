package com.cn.tbapi;

import com.alibaba.fastjson2.JSONObject;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.*;
import com.taobao.api.response.*;
import org.springframework.util.StringUtils;

/**
 * 淘宝商品发布
 */
public class GoodsPush {

    private static final String appkey = "";
    private static final String secret = "";
    private static final String url = "";

    /**
     * 获取淘宝店铺类目
     * https://open.taobao.com/api.htm?docId=161&docType=2&source=search
     */
    public String getTBCats(){
        TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
        ItemcatsAuthorizeGetRequest req = new ItemcatsAuthorizeGetRequest();
        req.setFields("brand.vid, brand.name");
        ItemcatsAuthorizeGetResponse rsp = null;
        try {
            rsp = client.execute(req, "sessionKey");
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return rsp.getBody();
    }

    /**
     * 商品发布
     * TODO 多线程发布
     * https://open.taobao.com/v2/doc?spm=4661s.15212433.0.0.2b3c669amDK5Qc#/apiFile?docType=2&docId=53962
     */
    public void taoGoodsPush(){
        TaobaoClient taobaoClient = new DefaultTaobaoClient(url,appkey,secret);
        AlibabaItemPublishSubmitRequest req = new AlibabaItemPublishSubmitRequest();
        req.setBizType("taobao/1.0.0/brandAsyncRenderEnable");
        req.setMarket("taobao");
        req.setCatId(1l);
        //req.setSpuId(32323L);
        req.setBarcode("6932529211107");
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

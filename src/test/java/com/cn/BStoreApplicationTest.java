package com.cn;

import com.alibaba.fastjson2.JSONObject;
import com.cn.domain.BookPull;
import com.cn.domain.Price;
import com.cn.service.IBookPullService;
import com.cn.service.IPriceService;
import com.cn.util.IPRandomUtil;
import com.cn.util.Utils;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.FileItem;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.DeliveryTemplate;
import com.taobao.api.domain.ItemCat;
import com.taobao.api.domain.SellerCat;
import com.taobao.api.request.*;
import com.taobao.api.response.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
//@EnableCaching
public class BStoreApplicationTest {

    /**
     * https://jsoup.org/cookbook/extracting-data/attributes-text-html
     */
    @Test
    public void test(){
        // 创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 创建GET请求
        HttpGet httpGet = new HttpGet("https://shop.kongfz.com/9128/type_1/?pagenum=2");
        httpGet.setHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36");
        httpGet.setHeader("Accept", "application/json, text/javascript, */*; q=0.01");
        httpGet.setHeader("Content-Type", "application/json;charset=UTF-8");
        httpGet.setHeader("Origin", "http://book.kongfz.com");
        httpGet.setHeader("Accept-Encoding", "gzip, deflate, br");
        //https://www.cnblogs.com/hefeng2014/p/17676298.html
        //httpGet.setHeader("Cookie","PHPSESSID=u6kqd7iat5g5c4fauj6eddc2n6; shoppingCartSessionId=f039288a6cb7058b6062ea241f7be91e; reciever_area=1006000000; kfz_uuid=12c34cab-8c68-4055-8740-d680ed32dab5; acw_tc=276077ca17125850647152511e10ecd089df56459d6d4783e3db6baa2085ab; kfz_trace=12c34cab-8c68-4055-8740-d680ed32dab5|0|e823178d5230a175|");
        httpGet.addHeader("x-forwarded-for", IPRandomUtil.getRandomIp());
        // 获取响应结果
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                String html = EntityUtils.toString(response.getEntity(), "UTF-8");
                // 创建Document对象
                Document document = Jsoup.parse(html);
                // 获取内容列表
                Element blog = document.getElementsByClass("result-list list-pic").first();
                Elements blogList = blog.getElementsByClass("item clearfix");
                for (Element element : blogList) {
                    String title = element.select("div.title a").text();
                    String author = element.select("div.zl-isbn-info span.text").text();
                    String price = element.attr("price");
                    String isbn = element.attr("isbn");
                    String quality = element.select("div.quality").text();
                    String publisher = "";
                    String shopid = element.attr("shopid");
                    String itemid = element.attr("itemid");
                    String img = element.select("img").first().attr("src");
                    /**
                     * 孔夫子书籍爬取列表有两种不同格式，爬取时需要针对不同情况做不同解析
                     */
                    List<String> info = element.select("div.normal-item span.normal-text").eachText();
                    List<String> tit = element.select("div.normal-item span.normal-title").eachText();
                    String remark;
                    JSONObject json = new JSONObject();
                    for(int i = 0;i<tit.size();i++){
                        json.put(tit.get(i).trim(),info.get(i).trim());
                    }
                    remark = json.toJSONString();
                    if(author == null || author.equals("")){
                        author = info.get(0);
                        publisher = info.get(1);
                        System.out.println("aaaaaa");
                    }else{
                        String[] as = author.split("/");
                        author = as[0].trim();
                        publisher = as[1].trim();
                    }
                    System.out.println("title: "+title+"\n author: "+author+"\n price: "+price+"\n img: "+img+"\n isbn: "+isbn+
                            "\n shopid: "+shopid+"\n itemid: "+itemid);

                    //将其写入数据库
                    //继续进行拉取操作
                }
            }
            httpClient.close();
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("test...");
    }

    @Test
    public void sp(){
        /*String s = "向红梅 肖凌云 陈家利 / 北京邮电大学出版社有限公司";
        String a = s.split("/")[0].trim();
        String p = s.split("/")[1].trim();
        Long id = 12345665l;
        System.out.println("id..."+id%10);*/


    }

    @Test
    public void pullTest(){
        long t = System.currentTimeMillis();
        List<JSONObject> list = reptile(9128l,"type_1",1);
        int page = 1;
        /*while (false){

            //将其写入数据库
            //FIXME 批量插入数据库
            //继续进行拉取操作
            if(list == null || list.size()<50){
                break;
            }
            System.out.println(page+"..本次拉取书籍数量：。。。"+list.size());
            list = reptile(6886l,"type_1",++page);
        }*/
        System.out.println("拉取总时间..."+(System.currentTimeMillis()- t));
    }

    @Test
    public void threadPool(){
        long t = System.currentTimeMillis();
        int num = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(num);
        AtomicInteger total = new AtomicInteger(0);
        for(int i=1;i<100000;i++){
            final int finalI = i;
            service.execute(new Runnable() {
                @Override
                public void run() {
                    List<JSONObject> list = reptile(9128l,"type_1", total.addAndGet(1));

                    if(list==null || list.size()<50){
                        service.shutdown();
                    }
                }
            });
        }
        System.out.println(total.get()+"拉取总时间..."+(System.currentTimeMillis()- t));
    }

    public List<JSONObject> reptile(Long storeId, String cat, int page){
        System.out.println("当前拉取数。。。"+page);
        /**
         * 孔夫子书籍爬取列表有两种不同格式，爬取时需要针对不同情况做不同解析
         */
        StringBuilder uri = new StringBuilder("https://shop.kongfz.com/"+storeId);
        if(!StringUtils.isEmpty(cat)){
            //https://shop.kongfz.com/9128/type_1/?pagenum=5
            uri.append("/"+cat+"/?pagenum="+page);
            if(page>100){
                return null;
            }
        }else {
            uri.append("/all/0_50_0_0_").append(page).append("_sort_desc_0_0/");
        }
        HttpGet httpGet = new HttpGet(uri.toString());
        // 创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建GET请求
        httpGet.setHeader("User-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36");
        httpGet.setHeader("Accept", "application/json, text/javascript, */*; q=0.01");
        httpGet.setHeader("Content-Type", "application/json;charset=UTF-8");
        httpGet.setHeader("Origin", "http://book.kongfz.com");
        httpGet.setHeader("Accept-Encoding", "gzip, deflate, br");
        httpGet.addHeader("x-forwarded-for", IPRandomUtil.getRandomIp());
        // 获取响应结果
        CloseableHttpResponse response = null;
        List<JSONObject> list = new ArrayList<>();
        try {
            response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                String html = EntityUtils.toString(response.getEntity(), "UTF-8");
                // 创建Document对象
                Document document = Jsoup.parse(html);
                // 获取内容列表
                Element blog = document.getElementsByClass("result-list list-pic").first();
                Elements blogList = blog.getElementsByClass("item clearfix");
                for (Element element : blogList) {
                    JSONObject json = new JSONObject();
                    json.put("title",element.select("div.title a").text());
                    String author = element.select("div.zl-isbn-info span.text").text();
                    json.put("author",element.select("div.zl-isbn-info span.text").text());
                    json.put("price",element.attr("price"));
                    json.put("isbn",element.attr("isbn"));
                    json.put("shopid",element.attr("shopid"));
                    json.put("itemid",element.attr("itemid"));
                    json.put("img",element.select("img").first().attr("src"));
                    List<String> info = element.select("div.normal-item span.normal-text").eachText();
                    List<String> tit = element.select("div.normal-item span.normal-title").eachText();
                    JSONObject remark = new JSONObject();
                    for(int i = 0;i<tit.size();i++){
                        remark.put(tit.get(i).trim(),info.get(i).trim());
                    }
                    if(author == null || author.equals("")){
                        json.put("author",info.get(0));
                        if(info.size()>1) {
                            json.put("publisher", info.get(1));
                        }
                    }else{
                        String[] as = author.split("/");
                        if(as.length>1) {
                            json.put("publisher", as[1].trim());
                        }
                        json.put("author",as[0].trim());
                    }
                    list.add(json);
                }
            }
            httpClient.close();
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Autowired
    IPriceService iPriceService;
    @Test
    public void priceTest(){
        Price price = Price.builder().id(1)
                .addPrice(new BigDecimal("00.00"))
                .endPrice(new BigDecimal("19.99"))
                .userId(123l)
                .build();
        iPriceService.update(price);
        //iPriceService.getAllList(123l);
    }

    @Autowired
    IBookPullService iBookPullService;

    @Test
    public void pull1Test(){
        BookPull pull = BookPull.builder()
                .name("test")
                .remark("test")
                .userId(123l)
                .status(0)
                .build();
        iBookPullService.save(pull);
        System.out.println("..."+pull.getId());
    }

    @Test
    public void cats(){
            TaobaoClient client = new DefaultTaobaoClient("http://39.98.70.213:30002/top/rest2", "13122675465", "0fb5104fd82969b7d591694104422bf8");
            ItemcatsAuthorizeGetRequest req = new ItemcatsAuthorizeGetRequest();
            req.setFields("brand.vid, brand.name");
            ItemcatsAuthorizeGetResponse rsp = null;
            try {
                rsp = client.execute(req, "610181057e897b8ba317eb84a9ac7c1a88e074b18fd13bd2506614820");
            } catch (ApiException e) {
                e.printStackTrace();
            }
            System.out.println(rsp.getBody());



    }

    @Test
    public void cat(){
        TaobaoClient client = new DefaultTaobaoClient("http://39.98.70.213:30002/top/rest2", "13122675465", "0fb5104fd82969b7d591694104422bf8");
        SellercatsListGetRequest req = new SellercatsListGetRequest();
        req.setFields("cid,name");
        SellercatsListGetResponse rsp = null;
        try {
            rsp = client.execute(req, "610181057e897b8ba317eb84a9ac7c1a88e074b18fd13bd2506614820");
        } catch (ApiException e) {
            e.printStackTrace();
        }
        List<SellerCat> res =  rsp.getSellerCats();
        System.out.println(rsp.getBody());
    }

    @Test
    public void getfee(){
        TaobaoClient client = new DefaultTaobaoClient("http://39.98.70.213:30002/top/rest2", "13122675465", "0fb5104fd82969b7d591694104422bf8");
        DeliveryTemplatesGetRequest req = new DeliveryTemplatesGetRequest();
        req.setFields("template_id,template_name,created,modified,supports,assumer,valuation,query_express,query_ems,query_cod,query_post");
        DeliveryTemplatesGetResponse rsp = null;
        try {
            rsp = client.execute(req, "6100402dfe1fb0bc8c2eb17198ec27fb71e78eee49336da2506614820");
        } catch (ApiException e) {
            e.printStackTrace();
        }
        List<DeliveryTemplate> list= rsp.getDeliveryTemplates();
        System.out.println(rsp.getBody());
    }

    @Test
    public void firstTemplate(){
        TaobaoClient client = new DefaultTaobaoClient("http://39.98.70.213:30002/top/rest2", "13122675465", "0fb5104fd82969b7d591694104422bf8");
        ShopcatsListGetRequest req = new ShopcatsListGetRequest();
        req.setFields("cid,parent_cid,name,is_parent");
        ShopcatsListGetResponse rsp = null;
        try {
            rsp = client.execute(req);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        System.out.println(rsp.getBody());
    }

    @Test
    public void catTemplate(){
        TaobaoClient client = new DefaultTaobaoClient("http://39.98.70.213:30002/top/rest2", "13122675465", "0fb5104fd82969b7d591694104422bf8");
        ItemcatsGetRequest req = new ItemcatsGetRequest();
        req.setFields("cid,parent_cid,name,is_parent");
        req.setParentCid(33l);
        ItemcatsGetResponse rsp = null;
        try {
            rsp = client.execute(req);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        List<ItemCat> list = rsp.getItemCats();
        System.out.println(rsp.getBody());
    }

    @Test
    public void push() throws ApiException {
        TaobaoClient client = new DefaultTaobaoClient("http://39.98.70.213:30002/top/rest2", "13122675465", "0fb5104fd82969b7d591694104422bf8");
        AlibabaItemPublishSubmitRequest req = new AlibabaItemPublishSubmitRequest();
        //req.setImages("https://img.alicdn.com/imgextra/i3/520557274/O1CN01noA5I023bXcMGWJXQ_!!0-item_pic.jpg");
       // req.setItemType("b");
        req.setBizType("taobao");
        req.setMarket("taobao");
        req.setCatId(1698370542l);
        req.setBarcode("6932529211107");
        AlibabaItemPublishSubmitResponse rsp = client.execute(req, "6100402dfe1fb0bc8c2eb17198ec27fb71e78eee49336da2506614820");
        System.out.println(rsp.getBody());
    }

    @Test
    public void pop() {
        TaobaoClient client = new DefaultTaobaoClient("http://39.98.70.213:30002/top/rest2", "13122675465", "0fb5104fd82969b7d591694104422bf8");
        ProductAddRequest req = new ProductAddRequest();
        req.setCid(50003112l);
        req.setName("测试商品");
        req.setPrice("999.99");
        req.setImage(new FileItem("/Users/pandawong/Desktop/test.jpg"));
        req.setOuterId("113129");
        req.setProps("50004788:1698370539");
        //req.setBinds("pid:vid;pid:vid");
        //req.setSaleProps("pid:vid;pid:vid");
        req.setCustomerProps("62031471780");
        req.setDesc("这是产品描述");
        req.setMajor(true);
        req.setNativeUnkeyprops("native_unkeyprops");
        req.setVerticalMarket(4L);
        ProductAddResponse rsp = null;
        try {
            rsp = client.execute(req, "6100402dfe1fb0bc8c2eb17198ec27fb71e78eee49336da2506614820");
        } catch (ApiException e) {
            e.printStackTrace();
        }
        System.out.println(rsp.getBody());
    }

    @Test
    public void prop() throws ApiException {
        TaobaoClient client = new DefaultTaobaoClient("http://39.98.70.213:30002/top/rest2", "13122675465", "0fb5104fd82969b7d591694104422bf8");
        AlibabaItemPublishPropsGetRequest req = new AlibabaItemPublishPropsGetRequest();
        req.setMarket("taobao");
        req.setCatId(50004658l);
        req.setBarcode("9787500878414");
        req.setSchema("<itemSchema> <field id=\"catProp\" name=\"类目属性\" type=\"complex\"> <complex-value> <field id=\"p-20000\" name=\"品牌\" type=\"singleCheck\"> <value>1128204128</value> </field> </complex-value> <fields> <field id=\"p-20000\" name=\"品牌\" type=\"singleCheck\"> </field> </fields> </field></itemSchema>");
        req.setPropId(20000L);
        AlibabaItemPublishPropsGetResponse rsp = client.execute(req, "6100402dfe1fb0bc8c2eb17198ec27fb71e78eee49336da2506614820");
        System.out.println(rsp.getBody());
    }

    @Test
    public void schema() throws ApiException {
        TaobaoClient client = new DefaultTaobaoClient("http://39.98.70.213:30002/top/rest2", "13122675465", "0fb5104fd82969b7d591694104422bf8");
        AlibabaItemPublishSchemaGetRequest req = new AlibabaItemPublishSchemaGetRequest();
        req.setImages("https://img.alicdn.com/imgextra/i3/520557274/O1CN01noA5I023bXcMGWJXQ_!!0-item_pic.jpg");
        req.setItemType("b");
        req.setBizType("taobao");
        req.setMarket("taobao");
        req.setCatId(50004725l);
        //req.setSpuId(32323L);
        req.setBarcode("6932529211107");
        AlibabaItemPublishSchemaGetResponse rsp = client.execute(req, "6100402dfe1fb0bc8c2eb17198ec27fb71e78eee49336da2506614820");
        System.out.println(rsp.getBody());
    }

    @Test
    public void price(){
        TaobaoClient client = new DefaultTaobaoClient("http://39.98.70.213:30002/top/rest2", "13122675465", "0fb5104fd82969b7d591694104422bf8");
        AlibabaItemOperateDownshelfRequest req = new AlibabaItemOperateDownshelfRequest();
        req.setItemId(792985854025l);
        AlibabaItemOperateDownshelfResponse rsp = null;
        try {
            rsp = client.execute(req, "6100402dfe1fb0bc8c2eb17198ec27fb71e78eee49336da2506614820");
        } catch (ApiException e) {
            e.printStackTrace();
        }
        System.out.println(rsp.getBody());
    }

    /**
     * 下架
     * https://open.taobao.com/api.htm?docId=53971&docType=2
     */
    @Test
    public void down(){
        TaobaoClient client = new DefaultTaobaoClient("http://39.98.70.213:30002/top/rest2", "13122675465", "0fb5104fd82969b7d591694104422bf8");
        AlibabaItemOperateDownshelfRequest req = new AlibabaItemOperateDownshelfRequest();
        req.setItemId(792985854025l);
        AlibabaItemOperateDownshelfResponse rsp = null;
        try {
            rsp = client.execute(req, "6100402dfe1fb0bc8c2eb17198ec27fb71e78eee49336da2506614820");
        } catch (ApiException e) {
            e.printStackTrace();
        }
        System.out.println(rsp.getBody());
    }

    @Test
    public void up(){
        TaobaoClient client = new DefaultTaobaoClient("http://39.98.70.213:30002/top/rest2", "13122675465", "0fb5104fd82969b7d591694104422bf8");
        AlibabaItemOperateUpshelfRequest req = new AlibabaItemOperateUpshelfRequest();
        req.setItemId(792985854025l);
        req.setQuantity(1l);
        AlibabaItemOperateUpshelfResponse rsp = null;
        try {
            rsp = client.execute(req, "6100402dfe1fb0bc8c2eb17198ec27fb71e78eee49336da2506614820");
        } catch (ApiException e) {
            e.printStackTrace();
        }
        System.out.println(rsp.getBody());
    }

    //50004727
    //50010485
    //1865157090

    /**
     * https://doc.fw199.com/docs/h7b/alibaba-item-publish-submit
     * @throws ApiException
     */
    @Test
    public void edit() throws ApiException {
        TaobaoClient client = new DefaultTaobaoClient("http://39.98.70.213:30002/top/rest2", "13122675465", "0fb5104fd82969b7d591694104422bf8");
        AlibabaItemPublishSubmitRequest req = new AlibabaItemPublishSubmitRequest();
        req.setBizType("taobao/1.0.0/brandAsyncRenderEnable");
        req.setMarket("taobao");
        req.setCatId(50010485l);
        //req.setSpuId(32323L);
        //req.setBarcode("6932529211107");

        String schema = "<itemSchema><field id=\"stuffStatus\" name=\"宝贝类型\" type=\"singleCheck\"><rules><rule name=\"requiredRule\" value=\"true\"/></rules><value>6</value><options><option displayName=\"全新\" value=\"5\" readonly=\"false\"/><option displayName=\"二手\" value=\"6\" readonly=\"false\"/></options></field><field id=\"title\" name=\"宝贝标题\" type=\"input\"><rules><rule name=\"tipRule\" value=\"标题和描述关键词是否违规自检工具：&lt;a href='//ss.taobao.com/compliance#/main' target='_blank'&gt;商品合规工具&lt;/a&gt;\"/><rule name=\"tipRule\" value=\"标题直接影响商品的搜索曝光机会，请&lt;a href='//market.m.taobao.com/app/qn/toutiao-new/index-pc.html#/detail/10682439?_k=rkwe5f' target='_blank'&gt;点此查看详情&lt;/a&gt;学习标题填写规范及优化知识\"/><rule name=\"tipRule\" value=\"即日起，标题中请勿使用制表符、换行符。若填入制表符、换行符，系统将自动替换成空格\"/><rule name=\"requiredRule\" value=\"true\"/><rule name=\"maxLengthRule\" value=\"60\" exProperty=\"include\" unit=\"byte\"/><rule name=\"tipRule\" value=\"最多允许输入30个汉字（60字符）\"/><rule name=\"valueTypeRule\" value=\"text\"/></rules><value>测试商品</value></field><field id=\"catProp\" name=\"类目属性\" type=\"complex\"><rules><rule name=\"tipRule\" value=\"若发布时遇到属性、属性值不能满足您提交商品的需求，请点击&lt;a href='https://cpv.taobao.com/report/categoryPropertyIssueReport.htm?categoryId=50010485' target='_blank'&gt;商品属性问题反馈&lt;/a&gt;\"/><rule name=\"tipRule\" value=\"请根据实际情况填写产品的重要属性，填写属性越完整，越有可能影响搜索流量，越有机会被消费者购买。&lt;a target = '_blank' href='https://market.m.taobao.com/app/qn/toutiao-new/index-pc.html#/detail/10671889?_k=3u6obw'&gt;了解更多&lt;/a&gt;\"/></rules><complex-value><field id=\"p-22370\" name=\"书刊种类\" type=\"singleCheck\"><value>79022260</value></field></complex-value></field><field id=\"globalStock\" name=\"采购地\" type=\"complex\"><rules><rule name=\"tipRule\" value=\"境外出版物代购类商品或服务属平台禁止发布的信息，&lt;a href='https://rule.taobao.com/detail-1077.htm?spm=a2177.7231205.0.0.153417eavsdJtc#6' target='_blank'&gt;点击查看详情&lt;/a&gt;\"/><rule name=\"requiredRule\" value=\"true\"/><rule name=\"readOnlyRule\" value=\"true\"/></rules><complex-value><field id=\"globalStockNav\" name=\"采购地\" type=\"singleCheck\"><value>0</value></field></complex-value><fields><field id=\"stockType\" name=\"库存类型\" type=\"singleCheck\"><rules><rule name=\"requiredRule\" value=\"true\"/><rule name=\"disableRule\" value=\"true\"><depend-group operator=\"and\"><depend-express fieldId=\"globalStockNav\" value=\"1\" symbol=\"!=\"/></depend-group></rule></rules><options><option displayName=\"现货（可快速发货）\" value=\"4738\" readonly=\"false\"/><option displayName=\"非现货（无现货，需采购）\" value=\"4674\" readonly=\"false\"/></options><value>4738</value></field></fields></field><field id=\"departurePlace\" name=\"发货地\" type=\"singleCheck\"><rules><rule name=\"disableRule\" value=\"true\"><depend-group operator=\"and\"><depend-express fieldId=\"globalStockNav\" value=\"1\" symbol=\"!=\"/></depend-group></rule><rule name=\"tipRule\" value=\"&lt;div&gt;&lt;div&gt;如发布宝贝需要展示海外直邮标识，保证全链路的物流信息完整，并符合平台的展示要求，在商品发布后，请到卖家中心－淘宝服务加入 海外直邮服务。&lt;br&gt;若您已签署服务合约，您可在卖家中心－淘宝服务 &lt;a href='https://xiaobao.taobao.com/service/itemManager.htm?spm=a215o.7394214.1.2.ksWzTq' target='_blank'&gt;服务商品管理&lt;/a&gt;，为符合服务承诺的商品添加海外直邮服务协议。&lt;br&gt;若您未签署服务合约，您可在卖家中心－淘宝服务 &lt;a href='https://xiaobao.taobao.com/service/serviceList.htm?spm=a215o.7394217.0.0.6fggRl' target='_blank'&gt;加入服务－海外直邮服务&lt;/a&gt;，签署海外直邮服务合约。&lt;/div&gt;&lt;/div&gt;\"><depend-group operator=\"and\"><depend-express fieldId=\"departurePlace\" value=\"1\" symbol=\"==\"/></depend-group></rule></rules><options><option displayName=\"中国内地（大陆）\" value=\"0\" readonly=\"false\"/><option displayName=\"中国港澳台地区及其他国家和地区\" value=\"1\" readonly=\"false\"/></options><value>0</value></field><field id=\"legalCustoms\" name=\"报关方式\" type=\"singleCheck\"><rules><rule name=\"disableRule\" value=\"true\"><depend-group operator=\"or\"><depend-express fieldId=\"globalStockNav\" value=\"1\" symbol=\"!=\"/><depend-express fieldId=\"departurePlace\" value=\"0\" symbol=\"!=\"/><depend-express fieldId=\"stockType\" value=\"4738\" symbol=\"!=\"/></depend-group></rule></rules><options><option displayName=\"一般贸易进口\" value=\"0\" readonly=\"false\"/><option displayName=\"其它\" value=\"1\" readonly=\"false\"/></options></field><field id=\"deliveryTimeType\" name=\"发货时效\" type=\"singleCheck\"><rules><rule name=\"requiredRule\" value=\"true\"/></rules><value>0</value></field><field id=\"tbDeliveryTime\" name=\"发货时间天数\" type=\"singleCheck\"><value>3</value></field><field id=\"price\" name=\"一口价\" type=\"input\"><value>100</value></field><field id=\"quantity\" name=\"总库存\" type=\"input\"><value>1</value></field><field id=\"images\" name=\"1:1主图\" type=\"complex\"><complex-value><field id=\"images_0\" name=\"1:1主图\" type=\"input\"><value>https://img.alicdn.com/imgextra/i2/2506614820/O1CN018yxJsE1lTbzVdBR6t_!!2506614820.jpg</value></field></complex-value></field><field id=\"desc\" name=\"PC端详情描述\" type=\"input\"><value>PC端详情描述</value></field><field id=\"wirelessDesc\" name=\"手机端详情描述\" type=\"input\"><value>手机端详情描述</value></field><field id=\"shopcat\" name=\"店铺中分类\" type=\"multiCheck\"><value>1698370542</value></field><field id=\"subStock\" name=\"拍下减库存\" type=\"singleCheck\"><value>1</value></field><field id=\"tbExtractWay\" name=\"运费\" type=\"complex\"><complex-value><field id=\"template\" name=\"运费模板\" type=\"input\"><value>1865157090</value></field></complex-value></field><field id=\"sevenDayNotSupport\" name=\"服务承诺：该类商品，不支持【七天退货】服务\" type=\"singleCheck\"><rules><rule name=\"tipRule\" value=\" 承诺更好服务可通过【&lt;a href='//xiaobao.taobao.com/service/serviceList.htm' target='_blank'&gt;交易合约&lt;/a&gt;】设置\"/><rule name=\"readOnlyRule\" value=\"true\"/></rules><value>true</value><options><option displayName=\"是\" value=\"true\"/><option displayName=\"否\" value=\"false\"/></options></field><field id=\"startTime\" name=\"上架时间\" type=\"singleCheck\"><value>0</value></field></itemSchema>";
        req.setSchema(schema);

        AlibabaItemPublishSubmitResponse rsp = client.execute(req, "6100402dfe1fb0bc8c2eb17198ec27fb71e78eee49336da2506614820");
        System.out.println(rsp.getBody());
    }

    /**
     * https://open.taobao.com/v2/doc?spm=1668v.15212433.0.0.c344669aNuHk2f#/apiFile?docType=2&docId=140
     * @throws ApiException
     */
    @Test
    public void picture() throws ApiException {
        TaobaoClient client = new DefaultTaobaoClient("http://39.98.70.213:30002/top/rest2", "13122675465", "0fb5104fd82969b7d591694104422bf8");
        PictureUploadRequest req = new PictureUploadRequest();
        //req.setPictureId(10000L);
        req.setPictureCategoryId(0l);
        req.setImg(new FileItem("/Users/pandawong/Desktop/test.jpg"));
        req.setImageInputTitle("test001.jpg");
        req.setTitle("测试图片001");
        req.setClientType("client:computer");
        req.setIsHttps(true);
        PictureUploadResponse rsp = client.execute(req, "6100402dfe1fb0bc8c2eb17198ec27fb71e78eee49336da2506614820");
        System.out.println(rsp.getBody());
    }

    @Test
    public void queryPic() throws ApiException {
        TaobaoClient client = new DefaultTaobaoClient("http://39.98.70.213:30002/top/rest2", "13122675465", "0fb5104fd82969b7d591694104422bf8");
        PictureUserinfoGetRequest req = new PictureUserinfoGetRequest();
        PictureUserinfoGetResponse rsp = client.execute(req, "6100402dfe1fb0bc8c2eb17198ec27fb71e78eee49336da2506614820");
        System.out.println(rsp.getBody());

    }

    /**
     * 处理水印图片
     */
    @Test
    public void handlePic() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String tb_seller_nick = "哈喽澳洲";
        //业务参数
        Map<String, String> data = new HashMap<String, String>();
        data.put("appid", "Mna7iFRbNg6gtCfP");
        data.put("tb_seller_nick", tb_seller_nick);
        Long timestamp = System.currentTimeMillis() / 1000;
        data.put("timestamp", timestamp.toString());
        // 必填 图片分类名称，最大长度20字符，中文字符算2个字符，不能为空
        data.put("picture_category_id", "0");
        data.put("image_input_title", "item_lb_img1.jpg");
        String base64ImageLogo = Utils.getBase64ImageFromBinary("/Users/pandawong/Desktop/test.jpg");
        data.put("base64_image", base64ImageLogo);
        // 非必填， 图片分类的父分类,一级分类的parent_id为0,二级分类的则为其父分类的picture_category_id
//        data.put("parent_id", "0");
        // 签名
        data.put("sign", Utils.Sign(data, "i74tyZwqgIw4qvb1"));
        // 调用服务API
        String result = Utils.doHttpRequest("https://kf.fw199.com/gateway/taobao/picture/upload", data);
        System.out.println(result);

    }

    public static Image readNetPic(String path){
        if(StringUtils.isEmpty(path)){
            return null;
        }
        try {
            String base64ImageLogo = Utils.getBase64ImageFromBinary("/Users/demodata/item_lb_img1.jpg");

            URL url = new URL(path);
            BufferedImage bufImg = ImageIO.read(url.openStream());
            return bufImg;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

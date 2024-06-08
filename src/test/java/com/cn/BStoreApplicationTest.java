package com.cn;

import com.alibaba.fastjson2.JSONObject;
import com.cn.domain.BookPull;
import com.cn.domain.Price;
import com.cn.service.IBookPullService;
import com.cn.service.IPriceService;
import com.cn.util.IPRandomUtil;
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

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
@EnableCaching
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
}

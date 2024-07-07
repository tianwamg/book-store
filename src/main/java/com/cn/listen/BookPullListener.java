package com.cn.listen;

import com.alibaba.fastjson2.JSONObject;
import com.cn.domain.BookInfo;
import com.cn.domain.BookPull;
import com.cn.service.IBookInfoService;
import com.cn.service.IBookPullService;
import com.cn.util.IPRandomUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class BookPullListener {

    private int coreNumber = Runtime.getRuntime().availableProcessors();

    @Autowired
    IBookInfoService iBookInfoService;

    @Autowired
    IBookPullService iBookPullService;

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 爬虫拉取书籍信息
     * @param msg
     */
    @RabbitListener(queues = "pullbook")
    public void receivePullBookMessage1(String msg){
        JSONObject json = JSONObject.parseObject(msg);
        String cancel = redisTemplate.opsForValue().get("task_cancel_"+json.getInteger("taskId")).toString();
        if(cancel != null){
            BookPull pull = BookPull.builder()
                    .id(json.getInteger("taskId"))
                    .status(1)
                    .build();
            iBookPullService.updateById(pull);
            System.out.println("receive1..."+msg);
            handleMessage(msg);

            pull.setStatus(2);
            iBookPullService.updateById(pull);
            System.out.println("receive1...complete");
            redisTemplate.delete("task_cancel_"+json.getInteger("taskId"));
        }


    }

    @RabbitListener(queues = "pullbook")
    public void receivePullBookMessage2(String msg){
        JSONObject json = JSONObject.parseObject(msg);
        String cancel = redisTemplate.opsForValue().get("task_cancel_"+json.getInteger("taskId")).toString();
        if(cancel != null){
            BookPull pull = BookPull.builder()
                    .id(json.getInteger("taskId"))
                    .status(1)
                    .build();
            iBookPullService.updateById(pull);
            System.out.println("receive2..."+msg);
            handleMessage(msg);
            pull.setStatus(2);
            iBookPullService.updateById(pull);
            System.out.println("receive2...complete");
            redisTemplate.delete("task_cancel_"+json.getInteger("taskId"));
        }
    }

    @RabbitListener(queues = "pullbook")
    public void receivePullBookMessage3(String msg){
        JSONObject json = JSONObject.parseObject(msg);
        String cancel = redisTemplate.opsForValue().get("task_cancel_"+json.getInteger("taskId")).toString();
        if(cancel != null){
            BookPull pull = BookPull.builder()
                    .id(json.getInteger("taskId"))
                    .status(1)
                    .build();
            iBookPullService.updateById(pull);
            System.out.println("receive3..."+msg);
            handleMessage(msg);
            pull.setStatus(2);
            iBookPullService.updateById(pull);
            System.out.println("receive3...complete");
            redisTemplate.delete("task_cancel_"+json.getInteger("taskId"));
        }

    }

    @RabbitListener(queues = "pullbook")
    public void receivePullBookMessage4(String msg){
        JSONObject json = JSONObject.parseObject(msg);
        String cancel = redisTemplate.opsForValue().get("task_cancel_"+json.getInteger("taskId")).toString();
        if(cancel != null){
            BookPull pull = BookPull.builder()
                    .id(json.getInteger("taskId"))
                    .status(1)
                    .build();
            iBookPullService.updateById(pull);
            System.out.println("receive4..."+msg);
            handleMessage(msg);
            pull.setStatus(2);
            iBookPullService.updateById(pull);
            System.out.println("receive4...complete");
            redisTemplate.delete("task_cancel_"+json.getInteger("taskId"));
        }
    }

    /**
     * 将其写入数据库
     * 继续进行拉取操作
     */
    public void handleMessage(String msg){
        JSONObject json = JSONObject.parseObject(msg);
        String cat = json.getString("cat");
        if(cat.equals("all")){
            cat = null;
        }
        List<BookInfo> list = reptile(json.getLong("storeId"),cat,1,json.getLong("userId"),json.getInteger("taskId"));
        int page = 1;
        while (true){
            iBookInfoService.saveBatch(list);
            if(list.size()<50){
                break;
            }
            list = reptile(json.getLong("storeId"),cat,++page,json.getLong("userId"),json.getInteger("taskId"));
        }
    }

    /**
     * 爬取网络内容
     */
    public List<BookInfo> reptile(Long storeId,String cat,int page,Long userId,int pullId){
        /**
         * 孔夫子书籍爬取列表有两种不同格式，爬取时需要针对不同情况做不同解析
         */
        StringBuilder uri = new StringBuilder("https://shop.kongfz.com/"+storeId);
        if(!StringUtils.isEmpty(cat)){
            uri.append("/"+cat+"/?pagenum="+page);
            //分类书籍最大拉取100页
            if(page>100){
                return new ArrayList<>();
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
        List<BookInfo> list = new ArrayList<>();
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
                    BookInfo bookInfo = new BookInfo();
                    bookInfo.setPullId(pullId);
                    bookInfo.setUserId(userId);
                    bookInfo.setTitle(element.select("div.title a").text().trim());
                    String author = element.select("div.zl-isbn-info span.text").text();
                    bookInfo.setPrice(new BigDecimal(element.attr("price")));
                    bookInfo.setIsbn(element.attr("isbn"));
                    bookInfo.setShopid(Integer.valueOf(element.attr("shopid")));
                    bookInfo.setItemid(element.attr("itemid"));
                    bookInfo.setImg(element.select("img").first().attr("src"));
                    bookInfo.setQuality(element.select("div.quality").text());
                    if(author == null || author.equals("")){
                        List<String> info = element.select("div.normal-item span.normal-text").eachText();
                        bookInfo.setAuthor(info.get(0));
                        if(info.size()>1) {
                            bookInfo.setPublisher(info.get(1));
                        }
                        bookInfo.setExtra(info.toString());
                    }else{
                        String[] as = author.split("/");
                        if(as.length>1) {
                            bookInfo.setPublisher(as[1].trim());
                        }
                        bookInfo.setAuthor(as[0].trim());
                        bookInfo.setExtra(author);

                    }
                    list.add(bookInfo);
                }
            }
            httpClient.close();
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}

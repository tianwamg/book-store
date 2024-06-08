package com.cn;

import com.alibaba.fastjson2.JSONObject;
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
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
public class ThreadTest {


    @Test
    public void threadPool() throws IOException {
        long t = System.currentTimeMillis();
        int num = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(num);
        AtomicInteger total = new AtomicInteger(0);
        AtomicInteger size= new AtomicInteger(50);
        while (size.get() == 50){
            service.submit(new Runnable() {
                @Override
                public void run() {
                    List<JSONObject> list = reptile(9128l, "type_1", total.incrementAndGet());

                    if(list == null || list.size()<50){
                        System.out.println("当前任务正在拉取中.."+total.get()+"..."+list.size());
                        size.set(0);
                    }
                }
            });
        }

        service.shutdown();

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
        }else {
            uri.append("/all/0_50_0_0_").append(page).append("_sort_desc_0_0/");
        }
        HttpGet httpGet ;
        // 创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        httpGet = new HttpGet(uri.toString());
        // 创建GET请求
        httpGet.setHeader("User-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36");
        httpGet.setHeader("Accept", "application/json, text/javascript, */*; q=0.01");
        httpGet.setHeader("Content-Type", "application/json;charset=UTF-8");
        httpGet.setHeader("Origin", "http://book.kongfz.com");
        httpGet.setHeader("Accept-Encoding", "gzip, deflate, br");
        httpGet.addHeader("x-forwarded-for", IPRandomUtil.getRandomIp());
        // 获取响应结果

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
                        json.put("publisher",info.get(1));
                    }else{
                        String[] as = author.split("/");
                        json.put("publisher",as[1].trim());
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
}

package com.cn.listen;

import cn.hutool.dfa.WordTree;
import com.alibaba.fastjson2.JSONObject;
import com.cn.common.Page;
import com.cn.domain.BookInfo;
import com.cn.domain.BookPull;
import com.cn.domain.Sensitive;
import com.cn.service.IBookInfoService;
import com.cn.service.IBookPullService;
import com.cn.service.ISensitiveService;
import com.cn.util.IPRandomUtil;
import com.github.houbb.sensitive.word.api.IWordDeny;
import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import com.rabbitmq.client.Channel;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class BookPullListener {

    private int coreNumber = Runtime.getRuntime().availableProcessors();

    @Autowired
    IBookInfoService iBookInfoService;

    @Autowired
    IBookPullService iBookPullService;

    @Autowired
    ISensitiveService iSensitiveService;

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 爬虫拉取书籍信息
     * @param msg
     */
    @RabbitListener(queues = "pullbook")
    public void receivePullBookMessage1(String msg, Channel channel, Message message) throws IOException {
        JSONObject json = JSONObject.parseObject(msg);
        if(redisTemplate.opsForValue().get("task_cancel_"+json.getInteger("taskId")) == null){
            String messageId = "pullmq_"+json.getInteger("taskId")+"_"+json.getLong("userId");
            if(redisTemplate.opsForValue().setIfAbsent(messageId,"0",7,TimeUnit.DAYS)) {

                BookPull pull = BookPull.builder()
                        .id(json.getInteger("taskId"))
                        .status(1)
                        .build();
                iBookPullService.updateById(pull);
                System.out.println("receive1..." + msg);
                handleMessage(msg);

                pull.setStatus(2);
                iBookPullService.updateById(pull);
                System.out.println("receive1...complete");
                redisTemplate.opsForValue().set(messageId,"1",7,TimeUnit.DAYS);
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);

            }else {
                if("1".equalsIgnoreCase((String)redisTemplate.opsForValue().get(messageId))){
                    channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
                }
            }
        }
    }

    @RabbitListener(queues = "pullbook")
    public void receivePullBookMessage2(String msg, Channel channel, Message message) throws IOException {
        JSONObject json = JSONObject.parseObject(msg);
        if(redisTemplate.opsForValue().get("task_cancel_"+json.getInteger("taskId")) == null){
            String messageId = "pullmq_"+json.getInteger("taskId")+"_"+json.getLong("userId");
            if(redisTemplate.opsForValue().setIfAbsent(messageId,"0",7,TimeUnit.DAYS)) {
                BookPull pull = BookPull.builder()
                        .id(json.getInteger("taskId"))
                        .status(1)
                        .build();
                iBookPullService.updateById(pull);
                System.out.println("receive2..." + msg);
                handleMessage(msg);
                pull.setStatus(2);
                iBookPullService.updateById(pull);
                System.out.println("receive2...complete");
                redisTemplate.opsForValue().set(messageId,"1",7,TimeUnit.DAYS);
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            }else {
                if("1".equalsIgnoreCase((String)redisTemplate.opsForValue().get(messageId))){
                    channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
                }
            }
        }
    }

    @RabbitListener(queues = "pullbook")
    public void receivePullBookMessage3(String msg, Channel channel, Message message) throws IOException {
        JSONObject json = JSONObject.parseObject(msg);
        if(redisTemplate.opsForValue().get("task_cancel_"+json.getInteger("taskId")) == null){
            String messageId = "pullmq_"+json.getInteger("taskId")+"_"+json.getLong("userId");
            if(redisTemplate.opsForValue().setIfAbsent(messageId,"0",7,TimeUnit.DAYS)) {
                BookPull pull = BookPull.builder()
                        .id(json.getInteger("taskId"))
                        .status(1)
                        .build();
                iBookPullService.updateById(pull);
                System.out.println("receive3..." + msg);
                handleMessage(msg);
                pull.setStatus(2);
                iBookPullService.updateById(pull);
                System.out.println("receive3...complete");
                redisTemplate.opsForValue().set(messageId,"1",7,TimeUnit.DAYS);
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            }else {
                if("1".equalsIgnoreCase((String)redisTemplate.opsForValue().get(messageId))){
                    channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
                }
            }
        }
    }

    @RabbitListener(queues = "pullbook")
    public void receivePullBookMessage4(String msg, Channel channel, Message message) throws IOException {
        JSONObject json = JSONObject.parseObject(msg);
        if(redisTemplate.opsForValue().get("task_cancel_"+json.getInteger("taskId")) == null){
            String messageId = "pullmq_"+json.getInteger("taskId")+"_"+json.getLong("userId");
            if(redisTemplate.opsForValue().setIfAbsent(messageId,"0",7,TimeUnit.DAYS)) {
                BookPull pull = BookPull.builder()
                        .id(json.getInteger("taskId"))
                        .status(1)
                        .build();
                iBookPullService.updateById(pull);
                System.out.println("receive4..." + msg);
                handleMessage(msg);
                pull.setStatus(2);
                iBookPullService.updateById(pull);
                System.out.println("receive4...complete");
                redisTemplate.opsForValue().set(messageId,"1",7,TimeUnit.DAYS);
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            }else {
                if("1".equalsIgnoreCase((String)redisTemplate.opsForValue().get(messageId))){
                    channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
                }
            }
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
        //SensitiveWordBs words = getSensitvieDBs(json.getLong("userId"));
        /*List<BookInfo> list = reptile(json.getLong("storeId"),cat,1,json.getLong("userId"),json.getInteger("taskId"),words);
        int page = 1;
        while (true){
            if(list !=null ){
                if(list.size() == 50){
                    iBookInfoService.saveBatch(list);
                }else{
                    if(list.size() == 1 && list.get(0).getStatus()==-2 && page<5000){//读取数据有误，跳过,读取有误数据最多拉取5000条数据
                        System.out.println("读取第n页数据有误,页书..."+page);
                    }else{//拉取最后数据
                        if(list.size()>0){
                            iBookInfoService.saveBatch(list);
                        }
                        break;
                    }

                }

            }
            list = reptile(json.getLong("storeId"),cat,++page,json.getLong("userId"),json.getInteger("taskId"),words);
        }*/
        List<BookInfo> list = new ArrayList<>();
        WordTree wordTree = wordTree(json.getLong("userId"));
        for(int i =1;i<1000;i++){
             list = reptile(json.getLong("storeId"),cat,i,json.getLong("userId"),json.getInteger("taskId"),wordTree);
            if(list != null && list.size() >0){
                iBookInfoService.saveBatch(list);

            }

        }
        list = null;
    }

    /**
     * 爬取网络内容
     */
    public List<BookInfo> reptile(Long storeId,String cat,int page,Long userId,int pullId,WordTree wordTree){
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
                if(blog == null){
                    BookInfo ninfo = new BookInfo();
                    ninfo.setTitle("拉取有误");
                    ninfo.setStatus(-2);
                    //list.add(ninfo);
                    return list ;
                }
                Elements blogList = blog.getElementsByClass("item clearfix");
                for (Element element : blogList) {
                    BookInfo bookInfo = new BookInfo();
                    bookInfo.setPullId(pullId);
                    bookInfo.setUserId(userId);
                    bookInfo.setTitle(element.select("div.title a").text().trim());
                    if(bookInfo.getTitle().length()>30){
                        continue;
                    }
                    String author = element.select("div.zl-isbn-info span.text").text();
                    bookInfo.setPrice(new BigDecimal(element.attr("price")));
                    bookInfo.setIsbn(element.attr("isbn"));
                    bookInfo.setShopid(Integer.valueOf(element.attr("shopid")));
                    bookInfo.setItemid(element.attr("itemid"));
                    bookInfo.setImg(element.select("img").first().attr("src"));
                    bookInfo.setQuality(element.select("div.quality").text());
                    if(author == null || author.equals("")){
                        List<String> info = element.select("div.normal-item span.normal-text").eachText();
                        bookInfo.setAuthor(info.get(0).trim());
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
                    String sens = wordTree.match(bookInfo.getTitle());
                    if(sens == null || sens.isEmpty()) {
                        list.add(bookInfo);
                    }
                }
            }
            httpClient.close();
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public SensitiveWordBs getSensitvieDBs(Long userId){
        return SensitiveWordBs.newInstance()
                .wordDeny(new IWordDeny() {
                    @Override
                    public List<String> deny() {
                        Page page = new Page();
                        int current =1 ;
                        page.setPageSize(50);
                        page.setCurrent(current);
                        Sensitive sensitive = new Sensitive();
                        sensitive.setUserId(userId);
                        sensitive.setStatus(1);
                        List<Sensitive> list = iSensitiveService.getPageList(page,sensitive).getRecords();
                        List<String> wordsList = new ArrayList<>();
                        while (list != null && list.size() >0){
                            wordsList.addAll(list.parallelStream().map(s -> s.getWord()).collect(Collectors.toList()));
                            current ++;
                            page.setCurrent(current);
                            list = iSensitiveService.getPageList(page,sensitive).getRecords();
                        }
                        return wordsList;
                    }
                })
                .ignoreChineseStyle(true)
                .ignoreEnglishStyle(true)
                .enableWordCheck(true)
                .init();
    }

    public WordTree wordTree(Long userId){
        WordTree sensitiveTree = new WordTree();
        List<String> list = ssl(userId);
        sensitiveTree.addWords(list);
        return sensitiveTree;
    }
    public List<String> ssl(Long userId){
        Page page = new Page();
        int current =1 ;
        page.setPageSize(50);
        page.setCurrent(current);
        Sensitive sensitive = new Sensitive();
        sensitive.setUserId(userId);
        sensitive.setStatus(1);
        List<Sensitive> list = iSensitiveService.getPageList(page,sensitive).getRecords();
        List<String> wordsList = new ArrayList<>();
        while (list != null && list.size() >0){
            wordsList.addAll(list.parallelStream().map(s -> s.getWord()).collect(Collectors.toList()));
            current ++;
            page.setCurrent(current);
            list = iSensitiveService.getPageList(page,sensitive).getRecords();
        }
        return wordsList;
    }

    public List<BookInfo> jsoupHtml(Long storeId,String cat,int page,Long userId,int pullId,WordTree wordTree){
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

        Proxy proxy = new Proxy(Proxy.Type.HTTP,new InetSocketAddress( IPRandomUtil.getRandomIp(),80));
        Connection connection = Jsoup.connect(uri.toString()).proxy(proxy);
        List<BookInfo> list = new ArrayList<>();
        try {
            //Connection.Response response = connection.method(Connection.Method.GET).execute();

            Document document =  Jsoup.connect(uri.toString()).proxy(proxy)
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .timeout(1000 * 30)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                    .header("accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                    .header("accept-encoding","gzip, deflate, br")
                    .header("accept-language","zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7")
                    .header("Origin", "http://book.kongfz.com")
                    .header("Accept-Encoding", "gzip, deflate, br")
                    .header("x-forwarded-for", IPRandomUtil.getRandomIp())
                    .get();
            // 获取内容列表
            Element blog = document.getElementsByClass("result-list list-pic").first();
            if(blog == null){
                BookInfo ninfo = new BookInfo();
                ninfo.setTitle("拉取有误");
                ninfo.setStatus(-2);
                //list.add(ninfo);
                return list ;
            }
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
                    bookInfo.setAuthor(info.get(0).trim());
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
                String sens = wordTree.match(bookInfo.getTitle());
                if(sens == null || sens.isEmpty()) {
                    list.add(bookInfo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}

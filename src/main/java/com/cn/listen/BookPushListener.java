package com.cn.listen;


import com.alibaba.fastjson2.JSONObject;
import com.cn.common.Page;
import com.cn.domain.BookInfo;
import com.cn.domain.Price;
import com.cn.domain.Waterprint;
import com.cn.dto.PushTaskDto;
import com.cn.request.CommonRequest;
import com.cn.service.IBookInfoService;
import com.cn.service.IPriceService;
import com.cn.service.IWaterprintService;
import com.cn.tbapi.SingletonClient;
import com.cn.tbapi.TaobaoApiStat;
import com.cn.tbapi.TaobaoService;
import com.taobao.api.ApiException;
import com.taobao.api.FileItem;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaItemPublishPropsGetRequest;
import com.taobao.api.request.AlibabaItemPublishSubmitRequest;
import com.taobao.api.request.PictureUploadRequest;
import com.taobao.api.response.AlibabaItemPublishPropsGetResponse;
import com.taobao.api.response.AlibabaItemPublishSubmitResponse;
import com.taobao.api.response.PictureUploadResponse;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Component
public class BookPushListener {

    @Autowired
    IWaterprintService iWaterprintService;

    @Autowired
    IBookInfoService iBookInfoService;

    @Autowired
    IPriceService iPriceService;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    TaobaoApiStat taobaoApiStat;

    /**
     * 向淘宝推送书籍任务
     * @param msg
     */
    @RabbitListener(queues = "pushbook")
    public void receivePushBookMsg1(String msg) throws Exception {
        System.out.println("书籍发布任务开始1...msg:..."+msg);
        JSONObject json = JSONObject.parseObject(msg);
        PushTaskDto pushTaskDto = JSONObject.parseObject(msg,PushTaskDto.class);
        goodsPush(pushTaskDto);
        System.out.println("书籍发布任务1...end:...");
    }

    @RabbitListener(queues = "pushbook")
    public void receivePushBookMsg2(String msg) throws Exception {
        System.out.println("书籍发布任务开始2...msg:..."+msg);
        JSONObject json = JSONObject.parseObject(msg);
        PushTaskDto pushTaskDto = JSONObject.parseObject(msg,PushTaskDto.class);
        goodsPush(pushTaskDto);
        System.out.println("书籍发布任务...2...end:...");
    }

    public void receivePushBookMsg3(String msg){
        System.out.println("书籍发布任务开始3...msg:..."+msg);
        JSONObject json = JSONObject.parseObject(msg);
    }

    public void receivePushBookMsg4(String msg){
        System.out.println("书籍发布任务开始4...msg:..."+msg);
        JSONObject json = JSONObject.parseObject(msg);
    }

    public void receivePushBookMsg5(String msg){
        System.out.println("书籍发布任务开始5...msg:..."+msg);
        JSONObject json = JSONObject.parseObject(msg);
    }

    public void receivePushBookMsg6(String msg) throws Exception {
        System.out.println("书籍发布任务开始6...msg:..."+msg);
        JSONObject json = JSONObject.parseObject(msg);
        PushTaskDto pushTaskDto = JSONObject.parseObject(msg,PushTaskDto.class);
        goodsPush(pushTaskDto);
    }




    public void goodsPush(PushTaskDto pushTaskDto) throws Exception{
        System.out.println("push task 1....");
        Object key = redisTemplate.opsForValue().get("taobao_key_"+pushTaskDto.getUserId());
        if(key == null){
            return;
        }
        System.out.println("push task 2....");
        String sessionKey = key.toString();
        List<Price> prices = getPrices(pushTaskDto.getUserId());
        Waterprint print = getPrints(pushTaskDto.getUserId());
        TaobaoClient client = SingletonClient.INSTANCE.getClient();
        List<BookInfo> nInfo = new ArrayList<>();
        int total = (int)Math.ceil((double) pushTaskDto.getPushNum()/50);
        int pageSize = 50;
        //FIXME
        System.out.println("push task 3....");
        for(int i = 0;i<total;i++){
            System.out.println("push task i:...."+i);
            nInfo.clear();
            if(i == total-1){
                pageSize = pushTaskDto.getPushNum()%50;
            }
            List<BookInfo> bookList = getBookList(i,pageSize,pushTaskDto.getUserId(), pushTaskDto.getTaskId());
            System.out.println("push task size:...."+bookList.size());
            if(pushTaskDto.getTitle()==null){
                pushTaskDto.setTitle("");
            }
            for(BookInfo n:bookList){
                //处理水印
                String path = "E:\\taobao\\"+pushTaskDto.getUserId()+"\\"+pushTaskDto.getTaskId()+n.getId()+".jpg";
                path = "/var/img/goodsprint/"+n.getUserId()+"/"+n.getPullId()+"/"+n.getId()+".jpg";
                File file = new File("/var/img/goodsprint/"+n.getUserId()+"/"+n.getPullId());
                if(!file.exists()){
                    file.mkdirs();
                }
                try {
                    imgwater(n.getImg(),print,path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //处理价格
                for(Price p: prices){
                    if(n.getPrice().compareTo(p.getStartPrice() )>=0 && n.getPrice().compareTo(p.getEndPrice())<=0){
                        if(p.getSimbol().equals("*")){
                            n.setPrice(n.getPrice().multiply(p.getAddPrice()));
                        }else {
                            n.setPrice(n.getPrice().add(p.getAddPrice()));
                        }
                        break;
                    }
                }
                //上传图片
                String pic = taobaoImgUpload(path,n.getTitle(),sessionKey);
                //上传商品
                AlibabaItemPublishSubmitRequest req = new AlibabaItemPublishSubmitRequest();
                req.setBizType("taobao/1.0.0/brandAsyncRenderEnable");
                req.setMarket("taobao");
                req.setCatId(50010485l);
                String title = pushTaskDto.getTitle().trim()+n.getTitle().trim()+"——"+n.getAuthor().trim();
                if(String_length(title)>60){
                    continue;
                }
                String schema = "<itemSchema>" +
                        "<field id=\"stuffStatus\" name=\"宝贝类型\" type=\"singleCheck\"><rules><rule name=\"requiredRule\" value=\"true\"/></rules><value>"+6+"</value><options><option displayName=\"全新\" value=\"5\" readonly=\"false\"/><option displayName=\"二手\" value=\"6\" readonly=\"false\"/></options></field>" +
                        "<field id=\"title\" name=\"宝贝标题\" type=\"input\"><rules><rule name=\"tipRule\" value=\"标题和描述关键词是否违规自检工具：&lt;a href='//ss.taobao.com/compliance#/main' target='_blank'&gt;商品合规工具&lt;/a&gt;\"/><rule name=\"tipRule\" value=\"标题直接影响商品的搜索曝光机会，请&lt;a href='//market.m.taobao.com/app/qn/toutiao-new/index-pc.html#/detail/10682439?_k=rkwe5f' target='_blank'&gt;点此查看详情&lt;/a&gt;学习标题填写规范及优化知识\"/><rule name=\"tipRule\" value=\"即日起，标题中请勿使用制表符、换行符。若填入制表符、换行符，系统将自动替换成空格\"/><rule name=\"requiredRule\" value=\"true\"/><rule name=\"maxLengthRule\" value=\"60\" exProperty=\"include\" unit=\"byte\"/><rule name=\"tipRule\" value=\"最多允许输入30个汉字（60字符）\"/><rule name=\"valueTypeRule\" value=\"text\"/></rules>" +
                        "<value>"+title+"</value></field>" +
                       // "<field id=\"catProp\" name=\"类目属性\" type=\"complex\"><rules><rule name=\"tipRule\" value=\"若发布时遇到属性、属性值不能满足您提交商品的需求，请点击&lt;a href='https://cpv.taobao.com/report/categoryPropertyIssueReport.htm?categoryId=50010485' target='_blank'&gt;商品属性问题反馈&lt;/a&gt;\"/>" +
                       // "<rule name=\"tipRule\" value=\"请根据实际情况填写产品的重要属性，填写属性越完整，越有可能影响搜索流量，越有机会被消费者购买。&lt;a target = '_blank' href='https://market.m.taobao.com/app/qn/toutiao-new/index-pc.html#/detail/10671889?_k=3u6obw'&gt;了解更多&lt;/a&gt;\"/>" +
                      //  "</rules><complex-value><field id=\"p-22370\" name=\"书刊种类\" type=\"singleCheck\"><value>"+pushTaskDto.getCat()+"</value></field>" +
                      //  "</complex-value></field>" +
                        "<field id=\"globalStock\" name=\"采购地\" type=\"complex\"><rules><rule name=\"tipRule\" value=\"境外出版物代购类商品或服务属平台禁止发布的信息，&lt;a href='https://rule.taobao.com/detail-1077.htm?spm=a2177.7231205.0.0.153417eavsdJtc#6' target='_blank'&gt;点击查看详情&lt;/a&gt;\"/><rule name=\"requiredRule\" value=\"true\"/><rule name=\"readOnlyRule\" value=\"true\"/></rules><complex-value>" +
                        "<field id=\"globalStockNav\" name=\"采购地\" type=\"singleCheck\"><value>0</value></field></complex-value><fields><field id=\"stockType\" name=\"库存类型\" type=\"singleCheck\"><rules><rule name=\"requiredRule\" value=\"true\"/><rule name=\"disableRule\" value=\"true\"><depend-group operator=\"and\"><depend-express fieldId=\"globalStockNav\" value=\"1\" symbol=\"!=\"/></depend-group></rule></rules><options><option displayName=\"现货（可快速发货）\" value=\"4738\" readonly=\"false\"/><option displayName=\"非现货（无现货，需采购）\" value=\"4674\" readonly=\"false\"/></options><value>4738</value></field></fields></field>" +
                        "<field id=\"departurePlace\" name=\"发货地\" type=\"singleCheck\"><rules><rule name=\"disableRule\" value=\"true\"><depend-group operator=\"and\"><depend-express fieldId=\"globalStockNav\" value=\"1\" symbol=\"!=\"/></depend-group></rule><rule name=\"tipRule\" value=\"&lt;div&gt;&lt;div&gt;如发布宝贝需要展示海外直邮标识，保证全链路的物流信息完整，并符合平台的展示要求，在商品发布后，请到卖家中心－淘宝服务加入 海外直邮服务。&lt;br&gt;若您已签署服务合约，您可在卖家中心－淘宝服务 &lt;a href='https://xiaobao.taobao.com/service/itemManager.htm?spm=a215o.7394214.1.2.ksWzTq' target='_blank'&gt;服务商品管理&lt;/a&gt;，为符合服务承诺的商品添加海外直邮服务协议。&lt;br&gt;若您未签署服务合约，您可在卖家中心－淘宝服务 &lt;a href='https://xiaobao.taobao.com/service/serviceList.htm?spm=a215o.7394217.0.0.6fggRl' target='_blank'&gt;加入服务－海外直邮服务&lt;/a&gt;，签署海外直邮服务合约。&lt;/div&gt;&lt;/div&gt;\"><depend-group operator=\"and\"><depend-express fieldId=\"departurePlace\" value=\"1\" symbol=\"==\"/></depend-group></rule></rules><options><option displayName=\"中国内地（大陆）\" value=\"0\" readonly=\"false\"/><option displayName=\"中国港澳台地区及其他国家和地区\" value=\"1\" readonly=\"false\"/></options><value>0</value></field>" +
                        "<field id=\"legalCustoms\" name=\"报关方式\" type=\"singleCheck\"><rules><rule name=\"disableRule\" value=\"true\"><depend-group operator=\"or\"><depend-express fieldId=\"globalStockNav\" value=\"1\" symbol=\"!=\"/><depend-express fieldId=\"departurePlace\" value=\"0\" symbol=\"!=\"/><depend-express fieldId=\"stockType\" value=\"4738\" symbol=\"!=\"/></depend-group></rule></rules><options><option displayName=\"一般贸易进口\" value=\"0\" readonly=\"false\"/><option displayName=\"其它\" value=\"1\" readonly=\"false\"/></options></field><field id=\"deliveryTimeType\" name=\"发货时效\" type=\"singleCheck\"><rules><rule name=\"requiredRule\" value=\"true\"/></rules><value>0</value></field><field id=\"tbDeliveryTime\" name=\"发货时间天数\" type=\"singleCheck\"><value>3</value></field>" +
                        "<field id=\"price\" name=\"一口价\" type=\"input\"><value>"+n.getPrice().doubleValue()+"</value></field>" +
                        "<field id=\"quantity\" name=\"总库存\" type=\"input\"><value>"+pushTaskDto.getStock()+"</value></field>" +
                        "<field id=\"images\" name=\"1:1主图\" type=\"complex\"><complex-value><field id=\"images_0\" name=\"1:1主图\" type=\"input\"><value>"+pic+"</value></field></complex-value></field>" +
                        "<field id=\"desc\" name=\"PC端详情描述\" type=\"input\"><value>"+pushTaskDto.getDesc()+"\\n"+n.getShopid()+"/"+n.getItemid()+"\\n"+n.getExtra()+"</value></field>" +
                        "<field id=\"wirelessDesc\" name=\"手机端详情描述\" type=\"input\"><value>"+pushTaskDto.getDesc()+"\\n"+n.getShopid()+"/"+n.getItemid()+"\n"+n.getExtra()+"</value></field>" +
                        "<field id=\"shopcat\" name=\"店铺中分类\" type=\"multiCheck\"><value>"+pushTaskDto.getSeller()+"</value></field>" +
                        "<field id=\"subStock\" name=\"拍下减库存\" type=\"singleCheck\"><value>1</value></field>" +
                        "<field id=\"tbExtractWay\" name=\"运费\" type=\"complex\"><complex-value><field id=\"template\" name=\"运费模板\" type=\"input\"><value>"+pushTaskDto.getFeeId()+"</value></field></complex-value></field>" +
                        //"<field id=\"sevenDayNotSupport\" name=\"服务承诺：该类商品，不支持【七天退货】服务\" type=\"singleCheck\"><rules><rule name=\"tipRule\" value=\" 承诺更好服务可通过【&lt;a href='//xiaobao.taobao.com/service/serviceList.htm' target='_blank'&gt;交易合约&lt;/a&gt;】设置\"/><rule name=\"readOnlyRule\" value=\"true\"/></rules><value>true</value><options><option displayName=\"是\" value=\"true\"/><option displayName=\"否\" value=\"false\"/></options></field>" +
                        "<field id=\"startTime\" name=\"上架时间\" type=\"singleCheck\"><value>"+pushTaskDto.getIsPush()+"</value></field></itemSchema>";
                req.setSchema(schema);
                AlibabaItemPublishSubmitResponse rsp = null;
                try {
                    rsp = client.execute(req, sessionKey);
                } catch (Exception e) {
                    //e.printStackTrace();
                }
                //System.out.println(rsp.getBody());
                BookInfo bookInfo = new BookInfo();
                bookInfo.setId(n.getId());
                bookInfo.setStatus(2);
                bookInfo.setUserId(n.getUserId());
                bookInfo.setTaobaoId(rsp.getItemId());
                bookInfo.setCatId(pushTaskDto.getSeller());
                iBookInfoService.updateStatus(bookInfo);
            }
        }
    }

    //获取书籍信息
    public List<BookInfo> getBookList(int current,int pageSize,Long userId,int pullId){
        Page page = new Page();
        page.setCurrent(current);
        page.setPageSize(pageSize);
        BookInfo info = new BookInfo();
        info.setUserId(userId);
        //info.setPullId(pullId);
        info.setStatus(1);
        CommonRequest<BookInfo> request = new CommonRequest<>();
        request.setRequestData(info);
        request.setPage(page);
        request.setUserId(userId);
        return iBookInfoService.getPageList(request).getRecords();
    }


    //处理价格
    public List<Price> getPrices(Long userId){
        return iPriceService.getAllList(userId);
    }
    //先处理水印图片，然后上传图片，再发布商品
    public Waterprint getPrints(Long userId){
         return  iWaterprintService.getAllList(userId);
    }

    public void imgwater(String net,Waterprint print,String path) throws IOException {
        BufferedImage li = readLocalPicture(print.getUrl());
        switch (print.getLocate()){
            case "content"://正面
                Thumbnails.of(new URL(net))
                        .forceSize(li.getWidth(),li.getHeight())
                        .watermark(Positions.CENTER,li,0.8f)
                        .outputQuality(1f)
                        .toFile(path);
                break;
            case "leftTop"://左上
                Thumbnails.of(new URL(net))
                        .forceSize(li.getWidth(),li.getHeight())
                        .watermark(Positions.TOP_LEFT,li,0.8f)
                        .outputQuality(1f)
                        .toFile(path);
                break;
            case "rightTop"://右上
                Thumbnails.of(new URL(net))
                        .forceSize(li.getWidth(),li.getHeight())
                        .watermark(Positions.TOP_RIGHT,li,0.8f)
                        .outputQuality(1f)
                        .toFile(path);
                break;
            case "leftBottom"://左下
                Thumbnails.of(new URL(net))
                        .size(li.getWidth(),li.getHeight())
                        .watermark(Positions.BOTTOM_LEFT,li,0.8f)
                        .outputQuality(1f)
                        .toFile(path);
                break;
            case "rightBottom"://右下
                Thumbnails.of(new URL(net))
                        .forceSize(li.getWidth(),li.getHeight())
                        .watermark(Positions.BOTTOM_RIGHT,li,0.8f)
                        .outputQuality(1f)
                        .toFile(path);
                break;
        }
    }

    /**
     * 读取本地图片
     * @param path
     * @return
     */
    public static BufferedImage readLocalPicture(String path) {
        if (null == path) {
            throw new RuntimeException("本地图片路径不能为空");
        }
        // 读取原图片信息 得到文件
        File srcImgFile = new File(path);
        try {
            // 将文件对象转化为图片对象
            return ImageIO.read(srcImgFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 读物网络图片
     * @param path
     * @return
     */
    public static Image readNetworkPicture(String path) {
        if (null == path) {
            throw new RuntimeException("网络图片路径不能为空");
        }
        try {
            // 创建一个URL对象,获取网络图片的地址信息
            URL url = new URL(path);
            // 将URL对象输入流转化为图片对象 (url.openStream()方法，获得一个输入流)
            BufferedImage bugImg = ImageIO.read(url.openStream());
            if (null == bugImg) {
                throw new RuntimeException("网络图片地址不正确");
            }
            return bugImg;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 淘宝图片上传
     * @param path
     * @param name
     * @param sessionKey
     * @return
     */
    public String taobaoImgUpload(String path,String name,String sessionKey){
        TaobaoClient client = SingletonClient.INSTANCE.getClient();
        PictureUploadRequest req = new PictureUploadRequest();
        //req.setPictureId(10000L);
        req.setPictureCategoryId(0l);
        req.setImg(new FileItem(path));
        req.setImageInputTitle(name+".jpg");//test.jpg
        req.setTitle(name);
        req.setClientType("client:computer");
        req.setIsHttps(true);
        PictureUploadResponse rsp = null;
        try {
            rsp = client.execute(req, sessionKey);//6100402dfe1fb0bc8c2eb17198ec27fb71e78eee49336da2506614820
        } catch (ApiException e) {
            e.printStackTrace();
        }
        //统计埋点
        taobaoApiStat.sendApiStat();
        return rsp.getPicture().getPicturePath();
    }

    public int String_length(String value){
        int val = 0;
        String chinese = "[\u4e00-\u9fa5]";
        for(int i=0;i<value.length();i++){
            String temp = value.substring(i,i+1);
            if(temp.matches(chinese)){
                val +=2;
            }else {
                val +=1;
            }
        }
        return val;
    }

}

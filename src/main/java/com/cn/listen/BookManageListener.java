package com.cn.listen;

import com.alibaba.fastjson2.JSONObject;
import com.cn.domain.BookInfo;
import com.cn.service.IBookInfoService;
import com.cn.tbapi.SingletonClient;
import com.taobao.api.ApiException;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaItemOperateDeleteRequest;
import com.taobao.api.response.AlibabaItemOperateDeleteResponse;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookManageListener {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    IBookInfoService iBookInfoService;

    @RabbitListener(queues = "deletebook")
    public void receiveGoodsDelete(String msg){
        JSONObject json = JSONObject.parseObject(msg);
        Object key = redisTemplate.opsForValue().get("taobao_key_"+json.getLong("userId"));
        if(key == null){
            return;
        }
        TaobaoClient client = SingletonClient.INSTANCE.getClient();
        String sessionKey = key.toString();
        //userid,catid,
        BookInfo info = new BookInfo();
        info.setUserId(json.getLong("userId"));
        info.setCatId(json.getLong("catId"));
        info.setStatus(1);
        int type = json.getInteger("type");
        if(type ==1){//删除选中分类
            int count = iBookInfoService.statusCount(info);
            count = (int)Math.ceil((double)count/50);
            for(int i =0;i<count;i++){
                //FIXME
                List<BookInfo> list = iBookInfoService.getStatusList(0,50,info);
                list.parallelStream().forEach(n ->{
                    taoGoodsDelete(n,client,sessionKey);
                });
            }
        }else{//剔除分类后删除
            int count = iBookInfoService.noCatCount(info);
            count = (int)Math.ceil((double)count/50);
            for(int i =0;i<count;i++){
                List<BookInfo> list = iBookInfoService.getNoCatList(0,50,info);
                list.parallelStream().forEach(n ->{
                    taoGoodsDelete(n,client,sessionKey);
                });
            }
        }
    }

    public void taoGoodsDelete(BookInfo info,TaobaoClient client,String sessionKey){
        AlibabaItemOperateDeleteRequest req = new AlibabaItemOperateDeleteRequest();
        req.setItemId(info.getTaobaoId());
        AlibabaItemOperateDeleteResponse rsp = null;
        try {
            rsp = client.execute(req, sessionKey);
        } catch (Exception e) {
            //e.printStackTrace();
        }
        System.out.println(rsp.isSuccess());
        if(rsp.isSuccess()){
            //FIXME
            BookInfo bookInfo = new BookInfo();
            bookInfo.setStatus(-1);
            bookInfo.setUserId(info.getUserId());
            bookInfo.setId(info.getId());
            iBookInfoService.delete(bookInfo);
        }
    }
}

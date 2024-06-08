package com.cn.listen;


import org.springframework.stereotype.Component;

@Component
public class BookPushListener {

    /**
     * 向淘宝推送书籍任务
     * @param msg
     */
    public void receivePushBookMsg(String msg){
        System.out.println("msg:..."+msg);
    }
}

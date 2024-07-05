package com.cn.tbapi;


import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;

/**
 * 淘宝client单例模式
 */
public enum SingletonClient {

    INSTANCE;

    private TaobaoClient client;

    private SingletonClient(){
        client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest","21310116","c6f32624a5374664bb8679ba8abc6e84");
    }

    public TaobaoClient getClient(){
        return client;
    }

}

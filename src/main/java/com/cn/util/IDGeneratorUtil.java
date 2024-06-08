package com.cn.util;

import com.baomidou.mybatisplus.extension.api.R;

import java.util.*;

/**
 * ID生成器
 */
public class IDGeneratorUtil {

    /**
     * 根据时间戳随机生成10位
     * @return
     */
    public static Long IDGenerator(){
        String str = String.valueOf(System.currentTimeMillis());
        List list = new ArrayList<>();
        for(Character s: str.toCharArray()){
            list.add(s.toString());
        }
        Random random = new Random();
        for(int i =0;i<2;i++){
            list.add(String.valueOf(random.nextInt(10)));
        }
        Collections.shuffle(list);
        return Long.valueOf(String.join("",list).toString().substring(4,14));
    }

    /**
     * UUID生成器
     * @return
     */
    public static Long UUIDGenerator(){
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        return Long.valueOf(str.substring(0,9));
    }


}

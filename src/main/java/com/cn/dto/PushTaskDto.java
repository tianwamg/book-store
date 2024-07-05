package com.cn.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class PushTaskDto implements Serializable {

    //任务id
    private int taskId;
    private Long userId;
    //模版分类
    private Long cat;
    //自定义分类
    private Long seller;
    //运费模版
    private Long feeId;
    //描述
    private String desc;
    //标题前缀
    private String title;
    //库存
    private int stock;
    //是否全新；全新：5。二手：6
    private int isNew;
    //是否发布;0：立即，2：仓库
    private int isPush;
}

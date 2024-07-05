package com.cn.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class PullTaskDto implements Serializable {

    private Long userId;
    private Long storeId;
    private String category;
}

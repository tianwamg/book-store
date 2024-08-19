package com.cn.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class PullTaskDto implements Serializable {

    private Long userId;
    private String storeId;
    private String category;
    private String startTime;
    private String endTime;
}

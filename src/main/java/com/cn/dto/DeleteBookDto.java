package com.cn.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class DeleteBookDto implements Serializable {

    private Long userId;
    private Long catId;
    private int type;
}

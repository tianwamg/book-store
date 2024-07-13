package com.cn.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class TaobaoCatDto implements Serializable {

    private Long cat;
    private String name;
}

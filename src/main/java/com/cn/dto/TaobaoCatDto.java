package com.cn.dto;

import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Builder
@Data
@AllArgsConstructor
public class TaobaoCatDto implements Serializable {

    private Long cat;
    private String name;
}

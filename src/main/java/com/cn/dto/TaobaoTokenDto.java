package com.cn.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TaobaoTokenDto implements Serializable {

    private int code;
    private int state;;
    private String accessToken;
    private String refreshToken;
    private Date expiresIn;
    private String shopNick;
    private Long expirationTime;
}

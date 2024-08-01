package com.cn.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TaobaoTokenDto implements Serializable {

    private String code;
    private String state;;
    private String accessToken;
    private String refreshToken;
    private Long expiresIn;
    private String shopNick;
    private String expirationTime;
}

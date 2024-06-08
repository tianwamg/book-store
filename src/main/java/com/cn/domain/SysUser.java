package com.cn.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class SysUser {

    private Long userId;
    private String name;
    private String password;
    private String phone;
    private String email;
    private Integer status;
    private Integer roleId;
    private Date expireTime;
    private Date ctime;
    private Date mtime;
}

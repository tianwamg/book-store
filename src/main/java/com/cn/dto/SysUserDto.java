package com.cn.dto;

import com.cn.domain.Menu;
import com.cn.domain.Role;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 登录用户信息
 */
@Builder
@Data
public class SysUserDto implements Serializable {

    private Long userId;
    private String name;
    private String phone;
    private String email;
    private Integer status;
    private Integer roleId;
    private Date expireTime;
    private Date ctime;
    private Date mtime;
    private String token;
    private Role role;
    private List<Menu> menuList;
}

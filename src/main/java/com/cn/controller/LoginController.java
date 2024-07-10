package com.cn.controller;

import com.alibaba.fastjson2.JSON;
import com.cn.anno.PassToken;
import com.cn.domain.Role;
import com.cn.domain.SysUser;
import com.cn.dto.SysUserDto;
import com.cn.request.CommonRequest;
import com.cn.response.ResultResponse;
import com.cn.service.IRoleService;
import com.cn.service.ISysUerService;
import com.cn.util.IDGeneratorUtil;
import com.cn.util.JwtUtil;
import com.cn.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户登陆注册模块
 * 此模块不做拦截与验证
 */

@RestController
@RequestMapping("/sys/login")
public class LoginController {

    @Autowired
    ISysUerService iSysUerService;

    @Autowired
    IRoleService iRoleService;

    @Autowired
    RedisTemplate<String,String> redisTemplate;

    @PassToken
    @PostMapping("/register")
    public ResultResponse<Boolean> register(@RequestBody CommonRequest<SysUser> request){
        SysUser sysUser = iSysUerService.getByUserIdOrName(request.getRequestData());
        if(sysUser != null ){
            return ResultResponse.error("501","当前用户已存在",null);
        }
        sysUser = request.getRequestData();
        SysUser user = SysUser.builder()
                .userId(IDGeneratorUtil.IDGenerator())
                .expireTime(new Date())
                .status(0)//待激活状态
                .roleId(3)//普通用户
                .name(sysUser.getName())
                .password(MD5Util.getMD5Digest(sysUser.getPassword()))//盐值加密
                .phone(sysUser.getPhone())
                .email(sysUser.getEmail())
                .build();
        boolean result = iSysUerService.save(user);
        return ResultResponse.success(result);
    }

    @PostMapping("/login")
    @PassToken
    public ResultResponse<SysUserDto> login(@RequestBody CommonRequest<SysUser> request){
        SysUser loginInfo = request.getRequestData();
        SysUser sysUser = iSysUerService.getByUserIdOrName(request.getRequestData());
        if(sysUser != null){
            String pwd = MD5Util.getMD5Digest(loginInfo.getPassword());
            if(!sysUser.getPassword().equals(pwd)){
                return ResultResponse.success("403","密码错误，请重新输入",null);
            }
            //判断是否为普通用户
            if(sysUser.getRoleId()>2){
                if(sysUser.getStatus() == 0){
                    return ResultResponse.success("403","当前用户未激活，请联系管理员",null);
                }
                //判断用户是否已过期
                if(sysUser.getExpireTime().getTime() < new Date().getTime()){
                    SysUser user = SysUser.builder()
                            .userId(sysUser.getUserId())
                            .status(0)
                            .build();
                    iSysUerService.updateUserInfo(user);
                    return ResultResponse.success("403","当前用户未激活，请联系管理员",null);
                }
            }

            //生成登陆信息
            /**
             * 1.获取角色信息
             * 2.获取权限
             * 3.生成登录token，为用户存储token于redis中
             */
            Map<String,String> map = new HashMap<>();
            map.put("userId",sysUser.getUserId().toString());
            map.put("roleId",sysUser.getRoleId().toString());
            String token = JwtUtil.getToken(map).substring(0,40);
            redisTemplate.opsForValue().set("login-"+token, JSON.toJSONString(sysUser));
            redisTemplate.expire("login-"+token, Duration.ofDays(7));
            Role role = iRoleService.getById(sysUser.getRoleId());
            SysUserDto sysUserDto = SysUserDto.builder()
                    .userId(sysUser.getUserId())
                    .name(sysUser.getName())
                    .roleId(sysUser.getRoleId())
                    .status(sysUser.getStatus())
                    .phone(sysUser.getPhone())
                    .email(sysUser.getEmail())
                    .expireTime(sysUser.getExpireTime())
                    .ctime(sysUser.getExpireTime())
                    .mtime(sysUser.getMtime())
                    .role(role)
                    .token(token)
                    .build();
            return ResultResponse.success(sysUserDto);
        }
        return ResultResponse.success("404","当前用户不存在",null);
    }
}

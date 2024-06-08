package com.cn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.domain.SysUser;
import com.cn.mapper.SysUserMapper;
import com.cn.request.CommonRequest;
import com.cn.service.ISysUerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * 系统用户
 */
@Service
public class ISysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUerService {

    @Autowired
    SysUserMapper sysUserMapper;

    public Page<SysUser> getPageList(CommonRequest<SysUser> request){
        SysUser sysUser = request.getRequestData();
        Page<SysUser> page = new Page<>(request.getPage().getCurrent(),request.getPage().getPageSize());
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        Optional.ofNullable(sysUser).ifPresent( u ->
                queryWrapper.lambda()
                        .eq(!StringUtils.isEmpty(sysUser.getUserId()),SysUser::getUserId,sysUser.getUserId())
                        .like(!StringUtils.isEmpty(sysUser.getName()),SysUser::getName,sysUser.getName())
                        .eq(!StringUtils.isEmpty(sysUser.getStatus()),SysUser::getStatus,sysUser.getStatus())
                        .eq(!StringUtils.isEmpty(sysUser.getRoleId()),SysUser::getRoleId,sysUser.getRoleId())
                        .eq(!StringUtils.isEmpty(sysUser.getPhone()),SysUser::getPhone,sysUser.getPhone())
                        .lt(!StringUtils.isEmpty(sysUser.getExpireTime()),SysUser::getExpireTime,sysUser.getExpireTime())
                        .orderByDesc(SysUser::getCtime)
        );
        return sysUserMapper.selectPage(page,queryWrapper);

    }

    @Override
    public SysUser getByUserIdOrName(SysUser sysUser) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(!StringUtils.isEmpty(sysUser.getName()),SysUser::getName,sysUser.getName())
                .eq(!StringUtils.isEmpty(sysUser.getUserId()),SysUser::getUserId,sysUser.getUserId());
        return sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public int updateUserInfo(SysUser sysUser){
        UpdateWrapper<SysUser> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.lambda().eq(SysUser::getUserId,sysUser.getUserId());
        return sysUserMapper.update(sysUser,userUpdateWrapper);
    }
}

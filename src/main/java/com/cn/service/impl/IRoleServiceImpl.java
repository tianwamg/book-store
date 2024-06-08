package com.cn.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.domain.Role;
import com.cn.mapper.RoleMapper;
import com.cn.request.CommonRequest;
import com.cn.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Ranger
 * @since 2024-03-19
 */
@CacheConfig(cacheNames = "role")
@Service
public class IRoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Autowired
    RoleMapper roleMapper;

    @Override
    public Page<Role> getPageList(CommonRequest<Role> request) {
        Page<Role> page = new Page<>(request.getPage().getCurrent(),request.getPage().getPageSize());
        QueryWrapper<Role> queryWrapper =new QueryWrapper<>();
        Optional.ofNullable(request.getRequestData()).ifPresent(r->
                 queryWrapper.lambda()
                        .eq(!StringUtils.isEmpty(r.getId()),Role::getId,r.getId())
                        .like(!StringUtils.isEmpty(r.getName()),Role::getName,r.getName())
                        .eq(!StringUtils.isEmpty(r.getStatus()),Role::getStatus,r.getStatus())

        );
        return roleMapper.selectPage(page,queryWrapper);
    }

    @Cacheable(cacheNames = "role",key = "#id")
    @Override
    public Role getRoleInfo(Integer id) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Role::getId,id)
                .eq(Role::getStatus,1);
        return roleMapper.selectOne(queryWrapper);
    }
}

package com.cn.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.domain.Menu;
import com.cn.domain.Permission;
import com.cn.mapper.MenuMapper;
import com.cn.mapper.PermissionMapper;
import com.cn.service.IPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author Ranger
 * @since 2024-03-20
 */
@CacheConfig(cacheNames = "menu")
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements IPermissionService {

    @Autowired
    PermissionMapper permissionMapper;

    @Autowired
    MenuMapper menuMapper;

    @CacheEvict(key = "roleId",allEntries = true)
    public int removeByRoleId(Integer roleId){
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Permission::getRoleId,roleId);
        return permissionMapper.delete(queryWrapper);
    }

    @Cacheable(cacheNames = "menu",key = "#roleId")
    public List<Menu> getMenuTreeList(Integer roleId){
        List<Menu> menus = getMenuList(roleId);
        //获取父节点
        List<Menu> list = menus.stream().filter(t -> t.getPid()==0).map(
                m -> {
                    m.setSubList(getSublist(m,menus));
                    return m;
                }
        ).collect(Collectors.toList());
        return list;
    }

    /**
     * 递归查询所有子节点
     * @param m
     * @param list
     * @return
     */
    public List<Menu> getSublist(Menu m,List<Menu> list){
        List<Menu> subList= list.stream().filter(t -> Objects.equals(t.getPid(),m.getId())).map(
                c ->{
                    c.setSubList(getSublist(c,list));
                    return c;
                }
        ).collect(Collectors.toList());
        return subList;
    }

    public List<Menu> getMenuList(Integer roleId){
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Permission::getRoleId,roleId);
        List<Integer> list = permissionMapper.selectList(queryWrapper)
                .stream().map(Permission::getMenuId).collect(Collectors.toList());
        QueryWrapper<Menu> menuQueryWrapper = new QueryWrapper<>();
        menuQueryWrapper.lambda().in(Menu::getId,list).orderByAsc(Menu::getId).orderByAsc(Menu::getLevel);
        return menuMapper.selectList(menuQueryWrapper);

    }
}

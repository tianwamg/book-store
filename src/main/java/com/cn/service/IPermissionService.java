package com.cn.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cn.domain.Menu;
import com.cn.domain.Permission;

import java.util.List;

/**
 * <p>
 * 权限表 服务类
 * </p>
 *
 * @author Ranger
 * @since 2024-03-20
 */
public interface IPermissionService extends IService<Permission> {

    /**
     * 根据角色删除权限表
     * @param roleId
     * @return
     */
    public int removeByRoleId(Integer roleId);

    /**
     * 获取权限菜单列表
     */
    public List<Menu> getMenuTreeList(Integer roleId);
}

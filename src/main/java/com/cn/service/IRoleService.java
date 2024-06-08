package com.cn.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cn.domain.Role;
import com.cn.request.CommonRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Ranger
 * @since 2024-03-19
 */
public interface IRoleService extends IService<Role> {

    public Page<Role> getPageList(CommonRequest<Role> request);

    public Role getRoleInfo(Integer id);
}

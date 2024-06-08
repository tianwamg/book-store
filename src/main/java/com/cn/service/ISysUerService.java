package com.cn.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cn.domain.SysUser;
import com.cn.request.CommonRequest;

public interface ISysUerService extends IService<SysUser> {

    /**
     * 获取用户分页列表
     * @param request
     * @return
     */
    public Page<SysUser> getPageList(CommonRequest<SysUser> request);

    /**
     * 根据用户id或名称
     * @param sysUser
     * @return
     */
    public SysUser getByUserIdOrName(SysUser sysUser);

    /**
     * 根据userId更新信息
     * @param sysUser
     * @return
     */
    public int updateUserInfo(SysUser sysUser);
}

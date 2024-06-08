package com.cn.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cn.domain.Sensitive;

/**
 * <p>
 * 敏感词 服务类
 * </p>
 *
 * @author Ranger
 * @since 2024-04-02
 */
public interface ISensitiveService extends IService<Sensitive> {

    public Page<Sensitive> getPageList(com.cn.common.Page page,Sensitive sensitive);

    public Integer update(Sensitive sensitive);

    public Integer delete(Sensitive sensitive);

}

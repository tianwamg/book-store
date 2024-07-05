package com.cn.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cn.domain.Waterprint;

import java.util.List;

/**
 * <p>
 * 水印 服务类
 * </p>
 *
 * @author Ranger
 * @since 2024-04-02
 */
public interface IWaterprintService extends IService<Waterprint> {

    public Page<Waterprint> getPageList(com.cn.common.Page page, Waterprint waterprint);

    public int update(Waterprint waterprint);

    public Waterprint getAllList(Long userId);

}

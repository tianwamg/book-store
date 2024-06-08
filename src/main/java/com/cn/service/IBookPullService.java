package com.cn.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cn.domain.BookPull;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Ranger
 * @since 2024-04-06
 */
public interface IBookPullService extends IService<BookPull> {

    public Page<BookPull> getPageList(com.cn.common.Page page,BookPull pull);

}

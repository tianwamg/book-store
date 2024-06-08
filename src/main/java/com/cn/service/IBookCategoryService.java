package com.cn.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cn.domain.BookCategory;
import com.cn.request.CommonRequest;

import java.util.List;

/**
 * <p>
 * 图书分类 服务类
 * </p>
 *
 * @author Ranger
 * @since 2024-03-30
 */
public interface IBookCategoryService extends IService<BookCategory> {

    public Page<BookCategory> getPageList(com.cn.common.Page page,BookCategory bookCategory);

    public List<BookCategory> getAllList();

    public List<BookCategory> getSubList(BookCategory category);


}

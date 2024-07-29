package com.cn.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cn.domain.BookInfo;
import com.cn.request.CommonRequest;

/**
 * <p>
 * 书籍库 服务类
 * </p>
 *
 * @author Ranger
 * @since 2024-05-03
 */
public interface IBookInfoService extends IService<BookInfo> {

    public Page<BookInfo> getPageList(CommonRequest<BookInfo> request);

    public int count(BookInfo info);

    public int delete(BookInfo bookInfo);

    public int updateStatus(BookInfo bookInfo);

    public void upload();

}

package com.cn.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cn.domain.BookInfo;
import com.cn.request.CommonRequest;

import java.util.List;

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

    //=======================================淘宝使用===============================//

    public List<BookInfo> getStatusList(int current,int pageSize,BookInfo bookInfo);

    public int statusCount(BookInfo info);

    public List<BookInfo> getNoCatList(int current,int pageSize,BookInfo bookInfo);

    public int noCatCount(BookInfo info);

    public int deleteTBPull(BookInfo bookInfo);

    public Page<BookInfo> getTBPageList(CommonRequest<BookInfo> request);

}

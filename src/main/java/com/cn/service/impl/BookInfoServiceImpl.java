package com.cn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.domain.BookInfo;
import com.cn.mapper.BookInfoMapper;
import com.cn.request.CommonRequest;
import com.cn.service.IBookInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * <p>
 * 书籍库 服务实现类
 * </p>
 *
 * @author Ranger
 * @since 2024-05-03
 */
@Service
public class BookInfoServiceImpl extends ServiceImpl<BookInfoMapper, BookInfo> implements IBookInfoService {

    @Autowired
    BookInfoMapper bookInfoMapper;

    @Override
    public Page<BookInfo> getPageList(CommonRequest<BookInfo> request) {
        Page<BookInfo> page = new Page<>(request.getPage().getCurrent(),request.getPage().getPageSize());
        QueryWrapper<BookInfo> queryWrapper = new QueryWrapper<>();
        Optional.ofNullable(request.getRequestData()).ifPresent(r->
                queryWrapper.lambda()
                    .eq(BookInfo::getUserId,request.getUserId())
                .eq(!StringUtils.isEmpty(r.getStatus()),BookInfo::getStatus,r.getStatus())
                .eq(!StringUtils.isEmpty(r.getPullId()),BookInfo::getPullId,r.getPullId())
                .orderByAsc(BookInfo::getId)
                );
        return bookInfoMapper.selectPage(page,queryWrapper);
    }

    @Override
    public int count(BookInfo info) {
        QueryWrapper<BookInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(BookInfo::getUserId,info.getUserId())
                .eq(!StringUtils.isEmpty(info.getStatus()),BookInfo::getStatus,info.getStatus())
                .eq(!StringUtils.isEmpty(info.getPullId()),BookInfo::getPullId,info.getPullId());

        return bookInfoMapper.selectCount(queryWrapper);

    }

    @Override
    public int delete(BookInfo bookInfo) {
        QueryWrapper<BookInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(BookInfo::getUserId,bookInfo.getUserId())
                .eq(BookInfo::getId,bookInfo.getId());
        return bookInfoMapper.delete(queryWrapper);

    }
}

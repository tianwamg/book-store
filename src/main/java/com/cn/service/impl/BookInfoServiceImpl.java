package com.cn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.domain.BookInfo;
import com.cn.domain.Sensitive;
import com.cn.mapper.BookInfoMapper;
import com.cn.request.CommonRequest;
import com.cn.service.IBookInfoService;
import org.apache.tomcat.jni.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.awt.print.Book;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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
                .like(!StringUtils.isEmpty(r.getTitle()),BookInfo::getTitle,r.getTitle())
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

    @Override
    public int updateStatus(BookInfo bookInfo){
        UpdateWrapper<BookInfo> updateWrapper= new UpdateWrapper<>();
        updateWrapper.lambda()
                .eq(BookInfo::getId,bookInfo.getId())
                .eq(BookInfo::getUserId,bookInfo.getUserId());
        return bookInfoMapper.update(bookInfo,updateWrapper);
    }

    @Async
    @Override
    public void upload() {

    }

    @Override
    public List<BookInfo> getStatusList(int current, int pageSize, BookInfo bookInfo) {
        Page<BookInfo> page = new Page<>(current,pageSize);
        QueryWrapper<BookInfo> queryWrapper = new QueryWrapper<>();
        Optional.ofNullable(bookInfo).ifPresent(r->
                queryWrapper.lambda()
                        .eq(BookInfo::getUserId,bookInfo.getUserId())
                        .ge(!StringUtils.isEmpty(r.getStatus()),BookInfo::getStatus,r.getStatus())
                        .eq(!StringUtils.isEmpty(r.getCatId()),BookInfo::getCatId,r.getCatId())
                        .orderByAsc(BookInfo::getId)
        );
        return bookInfoMapper.selectPage(page,queryWrapper).getRecords();
    }

    @Override
    public int statusCount(BookInfo info) {
        QueryWrapper<BookInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(BookInfo::getUserId,info.getUserId())
                .ge(!StringUtils.isEmpty(info.getStatus()),BookInfo::getStatus,info.getStatus())
                .eq(!StringUtils.isEmpty(info.getCatId()),BookInfo::getCatId,info.getCatId());
        return bookInfoMapper.selectCount(queryWrapper);

    }

    @Override
    public List<BookInfo> getNoCatList(int current,int pageSize,BookInfo bookInfo){
        Page<BookInfo> page = new Page<>(current,pageSize);
        QueryWrapper<BookInfo> queryWrapper = new QueryWrapper<>();
        Optional.ofNullable(bookInfo).ifPresent(r->
                queryWrapper.lambda()
                        .eq(BookInfo::getUserId,bookInfo.getUserId())
                        .ge(!StringUtils.isEmpty(r.getStatus()),BookInfo::getStatus,r.getStatus())
                        .notIn(!StringUtils.isEmpty(r.getCatId()),BookInfo::getCatId,r.getCatId())
                        .orderByAsc(BookInfo::getId)
        );
        return bookInfoMapper.selectPage(page,queryWrapper).getRecords();
    }

    @Override
    public int noCatCount(BookInfo info){
        QueryWrapper<BookInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(BookInfo::getUserId,info.getUserId())
                .ge(!StringUtils.isEmpty(info.getStatus()),BookInfo::getStatus,info.getStatus())
                .notIn(!StringUtils.isEmpty(info.getCatId()),BookInfo::getCatId,info.getCatId());
        return bookInfoMapper.selectCount(queryWrapper);
    }

    @Override
    public int deleteTBPull(BookInfo bookInfo){
        QueryWrapper<BookInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(BookInfo::getUserId,bookInfo.getUserId())
                .eq(BookInfo::getPullId,bookInfo.getPullId())
                .eq(BookInfo::getTaobaoId,0);
        return bookInfoMapper.delete(queryWrapper);
    }
}

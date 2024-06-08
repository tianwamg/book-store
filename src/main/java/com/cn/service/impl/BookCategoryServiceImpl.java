package com.cn.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.domain.BookCategory;
import com.cn.mapper.BookCategoryMapper;
import com.cn.request.CommonRequest;
import com.cn.service.IBookCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 图书分类 服务实现类
 * </p>
 *
 * @author Ranger
 * @since 2024-03-30
 */
@CacheConfig(cacheNames = "bookCategory")
@Service
public class BookCategoryServiceImpl extends ServiceImpl<BookCategoryMapper, BookCategory> implements IBookCategoryService {

    @Autowired
    BookCategoryMapper categoryMapper;

    @Cacheable(cacheNames = "bookCategory",key = "#page.current")
    @Override
    public Page<BookCategory> getPageList(com.cn.common.Page page,BookCategory bookCategory) {
        Page<BookCategory> p = new Page<>(page.getCurrent(),page.getPageSize());
        QueryWrapper<BookCategory> queryWrapper = new QueryWrapper();
        Optional.ofNullable(bookCategory).ifPresent(r->
                queryWrapper.lambda()
                        .eq(!StringUtils.isEmpty(r.getId()),BookCategory::getId,r.getCategory())
                        .like(!StringUtils.isEmpty(r.getName()),BookCategory::getName,r.getName())
                        .eq(!StringUtils.isEmpty(r.getCategory()),BookCategory::getCategory,r.getCategory())
                        .eq(BookCategory::getStatus,1)
                .eq(!StringUtils.isEmpty(r.getCategory()),BookCategory::getCategory,r.getCategory())
        );
        return categoryMapper.selectPage(p,queryWrapper);
    }

    @Cacheable(cacheNames = "bookCategory")
    @Override
    public List<BookCategory> getAllList() {
        QueryWrapper<BookCategory> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(BookCategory::getPid,0)
                .eq(BookCategory::getStatus,1);
        List<BookCategory> list = categoryMapper.selectList(queryWrapper);
        list.stream().forEach(b->{
            QueryWrapper<BookCategory> q = new QueryWrapper();
            q.lambda().eq(BookCategory::getPid,b.getId())
                    .eq(BookCategory::getStatus,1);
            List<BookCategory> sub = categoryMapper.selectList(q);
            b.setSubList(sub);
        });
        return list;
    }

    @Override
    public List<BookCategory> getSubList(BookCategory category) {
        QueryWrapper<BookCategory> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(!StringUtils.isEmpty(category.getId()),BookCategory::getId,category.getId())
                .eq(!StringUtils.isEmpty(category.getPid()),BookCategory::getPid,category.getPid())
                .eq(BookCategory::getStatus,1);
        return categoryMapper.selectList(queryWrapper);
    }
}

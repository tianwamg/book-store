package com.cn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.domain.BookPull;
import com.cn.mapper.BookPullMapper;
import com.cn.service.IBookPullService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Ranger
 * @since 2024-04-06
 */
@Service
public class BookPullServiceImpl extends ServiceImpl<BookPullMapper, BookPull> implements IBookPullService {

    @Autowired
    BookPullMapper bookPullMapper;

    @Override
    public Page<BookPull> getPageList(com.cn.common.Page page, BookPull pull) {
        Page<BookPull> p = new Page<>(page.getCurrent(),page.getPageSize());
        QueryWrapper<BookPull> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(BookPull::getUserId,pull.getUserId())
                .eq(!StringUtils.isEmpty(pull.getStatus()),BookPull::getStatus,pull.getStatus())
                .like(!StringUtils.isEmpty(pull.getName()),BookPull::getName,pull.getName());
        return bookPullMapper.selectPage(p,queryWrapper);
    }
}

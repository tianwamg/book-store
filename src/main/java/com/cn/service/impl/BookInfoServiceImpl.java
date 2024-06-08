package com.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.domain.BookInfo;
import com.cn.mapper.BookInfoMapper;
import com.cn.service.IBookInfoService;
import org.springframework.stereotype.Service;

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

}

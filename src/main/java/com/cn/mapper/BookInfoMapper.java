package com.cn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cn.domain.BookInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 书籍库 Mapper 接口
 * </p>
 *
 * @author Ranger
 * @since 2024-05-03
 */
@Mapper
public interface BookInfoMapper extends BaseMapper<BookInfo> {

}

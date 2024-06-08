package com.cn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cn.domain.BookCategory;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 图书分类 Mapper 接口
 * </p>
 *
 * @author Ranger
 * @since 2024-03-30
 */
@Mapper
@Repository
public interface BookCategoryMapper extends BaseMapper<BookCategory> {

}

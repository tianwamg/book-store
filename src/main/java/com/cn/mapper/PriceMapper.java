package com.cn.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cn.domain.Price;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 价格配置 Mapper 接口
 * </p>
 *
 * @author Ranger
 * @since 2024-04-02
 */
@Mapper
public interface PriceMapper extends BaseMapper<Price> {

}

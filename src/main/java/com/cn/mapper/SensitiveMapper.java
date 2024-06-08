package com.cn.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cn.domain.Sensitive;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 敏感词 Mapper 接口
 * </p>
 *
 * @author Ranger
 * @since 2024-04-02
 */
@Mapper
public interface SensitiveMapper extends BaseMapper<Sensitive> {

}

package com.cn.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cn.domain.Menu;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author Ranger
 * @since 2024-03-20
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

}

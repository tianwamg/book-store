package com.cn.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.domain.Menu;
import com.cn.mapper.MenuMapper;
import com.cn.service.IMenuService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author Ranger
 * @since 2024-03-20
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

}

package com.cn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.domain.Waterprint;
import com.cn.mapper.WaterprintMapper;
import com.cn.service.IWaterprintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 水印 服务实现类
 * </p>
 *
 * @author Ranger
 * @since 2024-04-02
 */
@Service
public class WaterprintServiceImpl extends ServiceImpl<WaterprintMapper, Waterprint> implements IWaterprintService {

    @Autowired
    WaterprintMapper waterprintMapper;

    @Override
    public Page<Waterprint> getPageList(com.cn.common.Page page, Waterprint waterprint) {
        Page<Waterprint> p = new Page<>(page.getCurrent(),page.getPageSize());
        QueryWrapper<Waterprint> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Waterprint::getUserId,waterprint.getUserId())
                .like(!StringUtils.isEmpty(waterprint.getName()),Waterprint::getName,waterprint.getName())
                .eq(!StringUtils.isEmpty(waterprint.getLocate()),Waterprint::getLocate,waterprint.getLocate());
        return waterprintMapper.selectPage(p,queryWrapper);
    }

    @Override
    public int update(Waterprint waterprint) {
        return waterprintMapper.updateById(waterprint);
    }
}

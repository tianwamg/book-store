package com.cn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.domain.Sensitive;
import com.cn.mapper.SensitiveMapper;
import com.cn.service.ISensitiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 敏感词 服务实现类
 * </p>
 *
 * @author Ranger
 * @since 2024-04-02
 */
@Service
public class SensitiveServiceImpl extends ServiceImpl<SensitiveMapper, Sensitive> implements ISensitiveService {

    @Autowired
    SensitiveMapper sensitiveMapper;

    @Override
    public Page<Sensitive> getPageList(com.cn.common.Page page, Sensitive sensitive) {
        Page<Sensitive> p = new Page<>(page.getCurrent(),page.getPageSize());
        QueryWrapper<Sensitive> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Sensitive::getUserId,sensitive.getUserId())
                .like(!StringUtils.isEmpty(sensitive.getWord()),Sensitive::getWord,sensitive.getWord())
                .eq(!StringUtils.isEmpty(sensitive.getStatus()),Sensitive::getStatus,sensitive.getStatus());
        return sensitiveMapper.selectPage(p,queryWrapper);
    }

    @Override
    public Integer update(Sensitive sensitive) {
        UpdateWrapper<Sensitive> updateWrapper= new UpdateWrapper<>();
        updateWrapper.lambda()
                .eq(Sensitive::getId,sensitive.getId())
                .eq(Sensitive::getUserId,sensitive.getUserId());
        return sensitiveMapper.update(sensitive,updateWrapper);
    }

    @Override
    public Integer delete(Sensitive sensitive) {
        QueryWrapper<Sensitive> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Sensitive::getUserId,sensitive.getUserId())
                .eq(Sensitive::getId,sensitive.getId());
        return sensitiveMapper.delete(queryWrapper);
    }
}

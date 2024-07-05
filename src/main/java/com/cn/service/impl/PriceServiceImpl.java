package com.cn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.domain.Price;
import com.cn.mapper.PriceMapper;
import com.cn.request.CommonRequest;
import com.cn.service.IPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 价格配置 服务实现类
 * </p>
 *
 * @author Ranger
 * @since 2024-04-02
 */
//@CacheConfig(cacheNames = "price")
@Service
public class PriceServiceImpl extends ServiceImpl<PriceMapper, Price> implements IPriceService {


    @Autowired
    PriceMapper priceMapper;

    //@Cacheable(cacheNames = "price",key = "#price.userId+'-'+#p.current")
    @Override
    public Page<Price> getPageList(com.cn.common.Page p, Price price) {
        Page<Price> page = new Page<>(p.getCurrent(),p.getPageSize());
        QueryWrapper<Price> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Price::getUserId,price.getUserId())
                .eq(!StringUtils.isEmpty(price.getStatus()),Price::getStatus,price.getStatus())
                .like(!StringUtils.isEmpty(price.getName()),Price::getName,price.getName());
        return priceMapper.selectPage(page,queryWrapper);
    }

    //@Cacheable(cacheNames = "price",key = "#userId")
    @Override
    public List<Price> getAllList(Long userId) {
        QueryWrapper<Price> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Price::getUserId,userId)
                .eq(Price::getStatus,1);
        return priceMapper.selectList(queryWrapper);
    }

    //@CacheEvict(cacheNames = "price",key = "#price.userId",allEntries = true)
    @Override
    public Integer update(Price price){
        return priceMapper.updateById(price);
    }

    //@CacheEvict(cacheNames = "price",key = "#price.userId",allEntries = true)
    @Override
    public Integer delete(Price price){
        return priceMapper.deleteById(price.getId());
    }
}

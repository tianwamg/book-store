package com.cn.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cn.domain.Price;
import com.cn.request.CommonRequest;

import java.util.List;

/**
 * <p>
 * 价格配置 服务类
 * </p>
 *
 * @author Ranger
 * @since 2024-04-02
 */
public interface IPriceService extends IService<Price> {

    public Page<Price> getPageList(com.cn.common.Page p, Price price);

    public List<Price> getAllList(Long userId);

    public Integer update(Price price);

    public Integer delete(Price price);


}

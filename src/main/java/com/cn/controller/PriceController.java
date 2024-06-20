package com.cn.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cn.domain.Price;
import com.cn.request.CommonRequest;
import com.cn.response.ResultResponse;
import com.cn.service.IPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 价格配置 前端控制器
 * </p>
 *
 * @author Ranger
 * @since 2024-04-02
 */
@RestController
@RequestMapping("/sys/price")
public class PriceController {

    @Autowired
    IPriceService iPriceService;

    @PostMapping("/pagelist")
    public ResultResponse<Page<Price>> getPageList(@RequestBody CommonRequest<Price> request){
        request.getRequestData().setUserId(request.getUserId());
        Page<Price> page = iPriceService.getPageList(request.getPage(), request.getRequestData());
        return ResultResponse.success(page);
    }

    @PostMapping("/insert")
    public ResultResponse<Boolean> insert(@RequestBody CommonRequest<Price> request){
        Price price = request.getRequestData();
        price.setUserId(request.getUserId());
        boolean result = iPriceService.save(request.getRequestData());
        return ResultResponse.success(result);
    }

    @PostMapping("/update")
    public ResultResponse<Integer> update(@RequestBody CommonRequest<Price> request){
        Price price = request.getRequestData();
        price.setUserId(request.getUserId());
        int result = iPriceService.update(request.getRequestData());
        return ResultResponse.success(result);
    }

    @PostMapping("/delete")
    public ResultResponse<Boolean> delete(@RequestBody CommonRequest<Price> request){
        boolean result = iPriceService.removeById(request.getRequestData().getId());
        return ResultResponse.success(result);
    }

}


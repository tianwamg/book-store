package com.cn.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cn.domain.Price;
import com.cn.domain.Waterprint;
import com.cn.request.CommonRequest;
import com.cn.response.ResultResponse;
import com.cn.service.IWaterprintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 水印 前端控制器
 * </p>
 *
 * @author Ranger
 * @since 2024-04-02
 */
@RestController
@RequestMapping("/sys/waterprint")
public class WaterprintController {

    @Autowired
    IWaterprintService iWaterprintService;

    @PostMapping("/pagelist")
    public ResultResponse<Page<Waterprint>> getPageList(@RequestBody CommonRequest<Waterprint> request){
        Page<Waterprint> page = iWaterprintService.getPageList(request.getPage(), request.getRequestData());
        return ResultResponse.success(page);
    }

    @PostMapping("/insert")
    public ResultResponse<Boolean> insert(@RequestBody CommonRequest<Waterprint> request){
        boolean result = iWaterprintService.save(request.getRequestData());
        return ResultResponse.success(result);
    }

    @PostMapping("/update")
    public ResultResponse<Integer> update(@RequestBody CommonRequest<Waterprint> request){
        int result = iWaterprintService.update(request.getRequestData());
        return ResultResponse.success(result);
    }

    @PostMapping("/delete")
    public ResultResponse<Boolean> delete(@RequestBody CommonRequest<Waterprint> request){
        boolean result = iWaterprintService.removeById(request.getRequestData().getId());
        return ResultResponse.success(result);
    }

}


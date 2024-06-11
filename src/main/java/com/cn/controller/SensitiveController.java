package com.cn.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cn.domain.Sensitive;
import com.cn.domain.Waterprint;
import com.cn.request.CommonRequest;
import com.cn.response.ResultResponse;
import com.cn.service.ISensitiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 敏感词 前端控制器
 * </p>
 *
 * @author Ranger
 * @since 2024-04-02
 */
@RestController
@RequestMapping("/sys/sensitive")
public class SensitiveController {

    @Autowired
    ISensitiveService iSensitiveService;

    @PostMapping("pagelist")
    public ResultResponse<Page<Sensitive>> getPageList(@RequestBody CommonRequest<Sensitive> request){
        Page<Sensitive> page = iSensitiveService.getPageList(request.getPage(),request.getRequestData());
        return ResultResponse.success(page);
    }


    @PostMapping("/insert")
    public ResultResponse<Boolean> insert(@RequestBody CommonRequest<Sensitive> request){
        boolean result = iSensitiveService.save(request.getRequestData());
        return ResultResponse.success(result);
    }

    @PostMapping("/update")
    public ResultResponse<Integer> update(@RequestBody CommonRequest<Sensitive> request){
        int result = iSensitiveService.update(request.getRequestData());
        return ResultResponse.success(result);
    }

    @PostMapping("/delete")
    public ResultResponse<Integer> delete(@RequestBody CommonRequest<Sensitive> request){
        Integer result = iSensitiveService.delete(request.getRequestData());
        return ResultResponse.success(result);
    }


}


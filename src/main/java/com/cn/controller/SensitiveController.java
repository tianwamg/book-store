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
        Sensitive sensitive = request.getRequestData();
        sensitive.setUserId(request.getUserId());
        Page<Sensitive> page = iSensitiveService.getPageList(request.getPage(),sensitive);
        return ResultResponse.success(page);
    }


    @PostMapping("/insert")
    public ResultResponse<Boolean> insert(@RequestBody CommonRequest<Sensitive> request){
        Sensitive sensitive = request.getRequestData();
        sensitive.setUserId(request.getUserId());
        boolean result = iSensitiveService.save(sensitive);
        return ResultResponse.success(result);
    }

    @PostMapping("/update")
    public ResultResponse<Integer> update(@RequestBody CommonRequest<Sensitive> request){
        Sensitive sensitive = request.getRequestData();
        sensitive.setUserId(request.getUserId());
        int result = iSensitiveService.update(sensitive);
        return ResultResponse.success(result);
    }

    @PostMapping("/delete")
    public ResultResponse<Integer> delete(@RequestBody CommonRequest<Sensitive> request){
        Sensitive sensitive = request.getRequestData();
        sensitive.setUserId(request.getUserId());
        Integer result = iSensitiveService.delete(sensitive);
        return ResultResponse.success(result);
    }


}


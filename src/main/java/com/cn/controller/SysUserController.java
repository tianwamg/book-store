package com.cn.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cn.domain.SysUser;
import com.cn.dto.SysUserDto;
import com.cn.request.CommonRequest;
import com.cn.response.ResultResponse;
import com.cn.service.ISysUerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/sys/user")
public class SysUserController {

    @Autowired
    ISysUerService iSysUerServicel;


    /**
     * 获取用户分页列表
     * @param request
     * @return
     */
    @PostMapping("/list")
    public ResultResponse<Page<SysUser>> getList(@RequestBody CommonRequest<SysUser> request){
        Page<SysUser> page = iSysUerServicel.getPageList(request);
        return ResultResponse.success(page);
    }

    /**
     * 待激活用户列表
     * @param request
     * @return
     */
    @PostMapping("/unactive")
    public ResultResponse<Page<SysUser>> getunactiveList(@RequestBody CommonRequest<SysUser> request){
        request.getRequestData().setStatus(0);
        Page<SysUser> page = iSysUerServicel.getPageList(request);
        return ResultResponse.success(page);
    }

    /**
     * 更新用户信息
     * @param request
     * @return
     */
    @PostMapping("/update")
    public ResultResponse<Integer> update(@RequestBody CommonRequest<SysUser> request){
        int result = iSysUerServicel.updateUserInfo(request.getRequestData());
        return ResultResponse.success(result);
    }

    /**
     * 激活用户
     * @param request
     * @return
     */
    @PostMapping("/active")
    public ResultResponse<Integer> active(@RequestBody CommonRequest<SysUser> request){
        SysUser sysUser = request.getRequestData();
        sysUser.setRoleId(3);
        sysUser.setStatus(1);
        int result = iSysUerServicel.updateUserInfo(sysUser);
        return ResultResponse.success(result);
    }


    /**
     * 查看用户详细信息
     * @param request
     * @return
     */
    @PostMapping("/info")
    public ResultResponse<SysUser> info(@RequestBody CommonRequest<SysUser> request){
        SysUser sysUser = iSysUerServicel.getByUserIdOrName(request.getRequestData());
        return ResultResponse.success(sysUser);
    }
}

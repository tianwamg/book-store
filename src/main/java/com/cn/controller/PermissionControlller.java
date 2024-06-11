package com.cn.controller;

import com.cn.domain.Menu;
import com.cn.domain.Permission;
import com.cn.request.CommonRequest;
import com.cn.response.ResultResponse;
import com.cn.service.IPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sys/permission")
public class PermissionControlller {

    @Autowired
    IPermissionService iPermissionService;

    /**
     * 删除权限
     */

    /**
     * 更新角色权限
     */
    @PostMapping("/update")
    public ResultResponse<Boolean> updatePermission(@RequestBody CommonRequest<List<Permission>> request){
        Permission permission = request.getRequestData().get(0);
        iPermissionService.removeByRoleId(permission.getRoleId());
        Boolean result = iPermissionService.saveBatch(request.getRequestData());
        return ResultResponse.success(result);
    }


    /**
     * 获取角色权限
     */
    @PostMapping("/list")
    public ResultResponse<List<Menu>> list(@RequestBody CommonRequest<Permission> request){
        List<Menu> list = iPermissionService.getMenuTreeList(request.getRequestData().getRoleId());
        return ResultResponse.success(list);
    }
}

package com.cn.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cn.domain.Role;
import com.cn.request.CommonRequest;
import com.cn.response.ResultResponse;
import com.cn.service.IPermissionService;
import com.cn.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Ranger
 * @since 2024-03-19
 */
@RestController
@RequestMapping("/sys/role")
public class RoleController {

    @Autowired
    IRoleService iRoleService;

    @Autowired
    IPermissionService iPermissionService;

    /**
     * 插入角色
     */
    @PostMapping("/insert")
    public ResultResponse<String> insertRole(@RequestBody CommonRequest<Role> request){
        String result= iRoleService.save(request.getRequestData()) ==true ?  "200": "500";
        return ResultResponse.success(result,"success",null);
    }

    /**
     * 获取角色分页列表
     * @param request
     * @return
     */
    @GetMapping("/list")
    public ResultResponse<Page<Role>> getList(@RequestBody CommonRequest<Role> request){
        Page<Role> page = iRoleService.getPageList(request);
        return ResultResponse.success(page);
    }

    /**
     *
     * xxx 删除角色 xxxx 废弃不允许使用
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public ResultResponse delete(@RequestBody CommonRequest<Role> request){
        Role role = request.getRequestData();
        role.setStatus(-1);
        boolean result = iRoleService.updateById(role);
        iPermissionService.removeByRoleId(role.getId());

        return ResultResponse.success(result);
    }

    /**
     *
     * 修改角色相关信息
     * @param request
     * @return
     */
    @PostMapping("/update")
    public ResultResponse updateRoleInfo(CommonRequest<Role> request){
        boolean result = iRoleService.updateById(request.getRequestData());
        return ResultResponse.success(result);
    }

}


package com.cn.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cn.domain.BookCategory;
import com.cn.request.CommonRequest;
import com.cn.response.ResultResponse;
import com.cn.service.IBookCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 图书分类 前端控制器
 * </p>
 *
 * @author Ranger
 * @since 2024-03-30
 */
@RestController
@RequestMapping("/sys/book-category")
public class BookCategoryController {

    @Autowired
    IBookCategoryService iBookCategoryService;

    /**
     * 获取分类分页列表
     * @param request
     * @return
     */
    @PostMapping("/pagelist")
    public ResultResponse<Page<BookCategory>> getPageList(@RequestBody CommonRequest<BookCategory> request){
        return ResultResponse.success(iBookCategoryService.getPageList(request.getPage(), request.getRequestData()));
    }

    /**
     * 获取全部分类列表
     * @param request
     * @return
     */
    @PostMapping("/alllist")
    public ResultResponse<List<BookCategory>> getAllList(@RequestBody CommonRequest request){
        return ResultResponse.success(iBookCategoryService.getAllList());
    }

    /**
     * 获取所有一级分类列表
     * @param request
     * @return
     */
    @PostMapping("plist")
    public ResultResponse<List<BookCategory>> getParentList(@RequestBody CommonRequest<BookCategory> request){
        BookCategory category = request.getRequestData();
        category.setPid(null);
        return ResultResponse.success(iBookCategoryService.getSubList(category));
    }

    /**
     * 获取所有子分类
     * @param request
     * @return
     */
    @PostMapping("sublist")
    public ResultResponse<List<BookCategory>> getChildList(@RequestBody CommonRequest<BookCategory> request){
        BookCategory category = request.getRequestData();
        category.setId(null);
        return ResultResponse.success(iBookCategoryService.getSubList(category));
    }

    @PostMapping("/insert")
    public ResultResponse insert(@RequestBody CommonRequest<BookCategory> request){
        return ResultResponse.success(iBookCategoryService.save(request.getRequestData()));
    }

    @PostMapping("/update")
    public ResultResponse update(@RequestBody CommonRequest<BookCategory> request){
        return ResultResponse.success(iBookCategoryService.updateById(request.getRequestData()));
    }

    @PostMapping("/delete")
    public ResultResponse delete(@RequestBody CommonRequest<BookCategory> request){
        BookCategory category = request.getRequestData();
        category.setStatus(-1);
        return ResultResponse.success(iBookCategoryService.updateById(category));
    }

}


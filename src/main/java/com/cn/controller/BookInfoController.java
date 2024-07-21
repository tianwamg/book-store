package com.cn.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cn.domain.BookInfo;
import com.cn.request.CommonRequest;
import com.cn.response.ResultResponse;
import com.cn.service.IBookInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 书籍库 前端控制器
 * </p>
 *
 * @author Ranger
 * @since 2024-05-03
 */
@RestController
@RequestMapping("/book/info")
public class BookInfoController {

    @Autowired
    IBookInfoService iBookInfoService;

    @PostMapping("/pagelist")
    public ResultResponse<Page<BookInfo>> getPageList(@RequestBody CommonRequest<BookInfo> request){
        return ResultResponse.success(iBookInfoService.getPageList(request));
    }

    @PostMapping("/delete")
    public ResultResponse<Integer> delete(@RequestBody CommonRequest<BookInfo> request){
        BookInfo bookInfo = request.getRequestData();
        bookInfo.setUserId(request.getUserId());
        return ResultResponse.success(iBookInfoService.delete(bookInfo));

    }

    @PostMapping("/upload")
    public ResultResponse upload(){
        iBookInfoService.upload();
        return ResultResponse.success(null);
    }
}


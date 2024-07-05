package com.cn.controller;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cn.domain.BookPull;
import com.cn.dto.PullTaskDto;
import com.cn.request.CommonRequest;
import com.cn.response.ResultResponse;
import com.cn.service.IBookPullService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 图书拉取
 */
@RestController
@RequestMapping("/book/pull")
public class BookPullController {

    @Autowired
    IBookPullService iBookPullService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping("/pagelist")
    public ResultResponse<Page<BookPull>> getPageList(@RequestBody CommonRequest<BookPull> request){
        request.getRequestData().setUserId(request.getUserId());
        Page<BookPull> page = iBookPullService.getPageList(request.getPage(),request.getRequestData());
        return ResultResponse.success(page);

    }

    @PostMapping("/buildtask")
    public ResultResponse buildTask(@RequestBody CommonRequest<PullTaskDto> request){
        //创建一个任务指令
        PullTaskDto pullTaskDto = request.getRequestData();
        BookPull pull = BookPull.builder()
                .name("来源书店:"+pullTaskDto.getStoreId())
                .remark("")
                .status(0)
                .userId(request.getUserId())
                .build();
        iBookPullService.save(pull);
        //将该指令放入mq
        //pull.getId();
        try {
            JSONObject object = new JSONObject();
            object.put("storeId",pullTaskDto.getStoreId());
            object.put("cat",pullTaskDto.getCategory());
            object.put("userId",request.getUserId());
            object.put("taskId",pull.getId());
            rabbitTemplate.convertAndSend("pullbook", object.toJSONString());
        }catch (Exception e){
            e.printStackTrace();
        }
        //返回待进行结果
        return ResultResponse.success("书籍拉取任务即将开始");
    }

    /**
     * 只有未开始任务可取消，一旦开始，任务拒绝结束
     * @param request
     * @return
     */
    @PostMapping("/canceltask")
    public ResultResponse cancelTask(@RequestBody CommonRequest request){
        return ResultResponse.success("");
    }

    /**
     * 重新开始任务
     * @param request
     * @return
     */
    @PostMapping("/rebuildtask")
    public ResultResponse rebuildTask(@RequestBody CommonRequest request){
        //创建一个任务指令
        //将该指令放入mq
        //返回待进行结果
        return ResultResponse.success("书籍拉取任务即将重新开始");
    }

}

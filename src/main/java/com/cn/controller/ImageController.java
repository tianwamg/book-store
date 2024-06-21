package com.cn.controller;

import com.cn.response.ResultResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;

@RestController
@RequestMapping("/sys/img")
public class ImageController {

    @PostMapping("/upload")
    public ResultResponse<String> upload(@RequestParam("file") MultipartFile file,@RequestParam("userId") Long userId){
        if(file.isEmpty()){
            return ResultResponse.error("404","图片上传为空");
        }
        String name = new Date().getTime()+"."+file.getOriginalFilename().split("\\.")[1];
        //TODO
        String path = "D:\\waterprint\\"  +userId+ "\\"+ name;
        try {
            file.transferTo(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultResponse.success(path);
    }
}

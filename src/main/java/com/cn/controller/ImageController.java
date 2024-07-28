package com.cn.controller;

import com.cn.domain.Sensitive;
import com.cn.response.ResultResponse;
import com.cn.service.ISensitiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Arrays;
import java.util.Date;

@RestController
@RequestMapping("/sys/img")
public class ImageController {

    @Autowired
    ISensitiveService sensitiveService;

    @PostMapping("/upload")
    public ResultResponse<String> upload(@RequestParam("file") MultipartFile file,@RequestParam("userId") Long userId){
        if(file.isEmpty()){
            return ResultResponse.error("404","图片上传为空");
        }
        String name = new Date().getTime()+"."+file.getOriginalFilename().split("\\.")[1];
        StringBuilder path = new StringBuilder("/var/img/waterprint/"  +userId);
        File f= new File(path.toString());
        if(!f.exists()){
            f.mkdirs();
        }
        try {
            file.transferTo(new File(path.append("/"+name).toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultResponse.success(path.toString());
    }

    @PostMapping("/sensitve")
    public ResultResponse sensitve(@RequestParam("file") MultipartFile file,@RequestParam("userId") Long userId){
        new Thread(() ->{
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    String[] tmp = line.split(",");
                    Arrays.stream(tmp).filter(str -> !str.trim().isEmpty()).parallel().forEach(n -> {
                        Sensitive sensitive = new Sensitive();
                        sensitive.setUserId(userId);
                        sensitive.setWord(n.trim());
                        sensitive.setStatus(1);
                        sensitiveService.save(sensitive);
                    });
                }
                reader.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        return ResultResponse.success(null);

    }
}

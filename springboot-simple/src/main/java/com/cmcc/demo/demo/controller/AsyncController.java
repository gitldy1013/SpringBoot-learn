package com.cmcc.demo.demo.controller;

import com.cmcc.demo.demo.service.AsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/async")
public class AsyncController {

    @Autowired
    private AsyncService asyncService;

    @Async
    @GetMapping("/index")
    public String asyncService(){
        asyncService.task();
        return "success";
    }

}

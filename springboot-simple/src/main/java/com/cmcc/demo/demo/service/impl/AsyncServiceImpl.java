package com.cmcc.demo.demo.service.impl;

import com.cmcc.demo.demo.service.AsyncService;
import org.springframework.stereotype.Service;

@Service
public class AsyncServiceImpl implements AsyncService {

    public void task(){
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

package com.cdxt.batchinsert.controller;

import com.cdxt.batchinsert.entity.TestInfo;
import com.cdxt.batchinsert.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {
    @Autowired
    private TestService testService;

    @GetMapping("/list")
    public List<TestInfo> findAll(){
        List<TestInfo> res= testService.findAll();
        System.out.println("find all:"+res);
        return res;
    }
}

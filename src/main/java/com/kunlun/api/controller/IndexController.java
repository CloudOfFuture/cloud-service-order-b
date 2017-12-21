package com.kunlun.api.controller;

import com.kunlun.result.DataRet;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author by kunlun
 * @version <0.1>
 * @created on 2017/12/20.
 */
@RestController
@RequestMapping("index")
public class IndexController {


    @GetMapping("/test")
    public DataRet<String> test(){
        return new DataRet<>("这是服务B");
    }
}

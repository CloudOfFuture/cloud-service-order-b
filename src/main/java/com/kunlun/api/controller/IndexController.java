package com.kunlun.api.controller;

import com.kunlun.result.DataRet;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author by kunlun
 * @version <0.1>
 * @created on 2017/12/20.
 */
@RestController
@RequestMapping("index")
public class IndexController {


    /**
     * 测试服务之间调用
     *
     * @return
     */
    @GetMapping("/log")
    public DataRet<String> logTest(@RequestParam("orderNo") String orderNo) {
        return new DataRet<>("我是服务B：orderNo=" + orderNo);
    }
}

package com.kunlun.api.client;

import com.kunlun.api.hystrix.SendGoodClientHystrix;
import com.kunlun.entity.SendGood;
import com.kunlun.result.DataRet;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author by fk
 * @version <0.1>
 * @created on 2018-01-12.
 */
@FeignClient(value = "cloud-service-common", fallback = SendGoodClientHystrix.class)
public interface SendGoodClient {

    /**
     * 查询发货信息
     *
     * @param sendGoodId
     * @return
     */
    @GetMapping(value = "findById")
    DataRet<SendGood> findById(@RequestParam(value = "sendGoodId") Long sendGoodId);
}

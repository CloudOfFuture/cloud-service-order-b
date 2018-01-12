package com.kunlun.api.client;

import com.kunlun.api.hystrix.GoodClientHystrix;
import com.kunlun.result.DataRet;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author ycj
 * @version V1.0 <>
 * @date 2018-01-05 16:15
 */
@FeignClient(value = "cloud-service-good", fallback = GoodClientHystrix.class)
public interface GoodClient {

    /**
     * 修改库存
     *
     * @param id    Long
     * @param count Integer
     * @return DataRet
     */
    @PostMapping("/backstage/good/updateStock")
    DataRet updateStock(@RequestParam("id") Long id,
                        @RequestParam("count") Integer count);
}

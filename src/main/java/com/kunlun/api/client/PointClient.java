package com.kunlun.api.client;

import com.kunlun.api.hystrix.PointClientHystrix;
import com.kunlun.entity.Point;
import com.kunlun.result.DataRet;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author ycj
 * @version V1.0 <>
 * @date 2018-01-05 16:08
 */
@FeignClient(value = "cloud-service-user-center", fallback = PointClientHystrix.class)
public interface PointClient {

    /**
     * 修改积分
     *
     * @param userId String
     * @param point  Integer
     * @return DataRet
     */
    @PostMapping("/point/updatePoint")
    DataRet updatePoint(@RequestParam("userId") String userId,
                        @RequestParam("point") Integer point);

    /**
     * 查询用户积分
     *
     * @param userId String
     * @return DataRet
     */
    @GetMapping("/point/findPointByUserId")
    DataRet<Point> findByUserId(@RequestParam("userId") String userId);
}

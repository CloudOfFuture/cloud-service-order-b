package com.kunlun.api.hystrix;

import com.kunlun.api.client.PointClient;
import com.kunlun.entity.Point;
import com.kunlun.result.DataRet;
import org.springframework.stereotype.Component;

/**
 * @author ycj
 * @version V1.0 <>
 * @date 2018-01-05 16:09
 */
@Component
public class PointClientHystrix implements PointClient {
    @Override
    public DataRet updatePoint(String userId, Integer operatePoint) {
        return new DataRet("ERROR", "请求失败");
    }

    @Override
    public DataRet<Point> findByUserId(String userId) {
        return new DataRet<>("ERROR", "请求失败");
    }
}

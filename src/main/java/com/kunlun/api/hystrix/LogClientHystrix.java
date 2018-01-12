package com.kunlun.api.hystrix;

import com.alibaba.fastjson.JSONArray;
import com.kunlun.api.client.LogClient;
import com.kunlun.entity.GoodLog;
import com.kunlun.entity.OrderLog;
import com.kunlun.entity.PointLog;
import com.kunlun.result.DataRet;
import org.springframework.stereotype.Component;

/**
 * @author ycj
 * @version V1.0 <>
 * @date 2018-01-05 14:38
 */
@Component
public class LogClientHystrix implements LogClient {
    @Override
    public DataRet<String> addOrderLog(OrderLog orderLog) {
        return new DataRet<>("ERROR", "请求失败");
    }

    @Override
    public DataRet<String> addGoodLog(GoodLog goodLog) {
        return new DataRet<>("ERROR", "请求失败");
    }

    @Override
    public DataRet<String> addPointLog(PointLog pointLog) {
        return new DataRet<>("ERROR", "请求失败");
    }

    @Override
    public DataRet<String> addPointLogList(JSONArray jsonArray) {
        return new DataRet<>("ERROR", "请求失败");
    }
}

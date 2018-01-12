package com.kunlun.api.hystrix;

import com.kunlun.api.client.GoodClient;
import com.kunlun.result.DataRet;
import org.springframework.stereotype.Component;

/**
 * @author ycj
 * @version V1.0 <>
 * @date 2018-01-05 16:15
 */
@Component
public class GoodClientHystrix implements GoodClient {
    @Override
    public DataRet updateStock(Long goodId, Integer stock) {
        return new DataRet("ERROR", "请求失败");
    }
}

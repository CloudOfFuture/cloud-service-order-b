package com.kunlun.api.hystrix;

import com.kunlun.api.client.SendGoodClient;
import com.kunlun.entity.SendGood;
import com.kunlun.result.DataRet;
import org.springframework.stereotype.Component;

/**
 * @author by fk
 * @version <0.1>
 * @created on 2018-01-12.
 */
@Component
public class SendGoodClientHystrix implements SendGoodClient {

    /**
     * 查询发货信息
     *
     * @param sendGoodId
     * @return
     */
    @Override
    public DataRet<SendGood> findById(Long sendGoodId) {
        return new DataRet<>("ERROR", "查询发货信息失败");
    }
}

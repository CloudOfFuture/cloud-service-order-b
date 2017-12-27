package com.kunlun.api.service;

import com.kunlun.result.DataRet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author by kunlun
 * @version <0.1>
 * @created on 2017/12/21.
 */
@Service
public class IndexServiceImpl implements IndexService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public DataRet<String> logTest(Long orderId) {
        LOGGER.info("cloud-service-order接收到参数：" + orderId);
        LOGGER.info("RestTemplate发送请求到Log服务");
        restTemplate.getForObject("http://cloud-ribbon-server/api/ribbon/log?order_id=" + orderId, DataRet.class);
        return new DataRet<>("调用完成");
    }
    
}

package com.kunlun.api.service;

import com.kunlun.entity.Order;
import com.kunlun.result.DataRet;
import com.kunlun.result.PageResult;

/**
 * @author by kunlun
 * @version <0.1>
 * @created on 2017/12/20.
 */
public interface WxOrderService {

    /**
     * 订单列表
     *
     * @param pageNo
     * @param pageSize
     * @param wxCode
     * @param orderStatus
     * @param payType
     * @return
     */
    PageResult findByOpenid(Integer pageNo, Integer pageSize, String wxCode, String orderStatus, String payType);

    /**
     * 申请退款
     *
     * @param orderId
     * @return
     */
    DataRet<String> refund(Long orderId);

    /**
     * 查询订单详情
     *
     * @param orderId
     * @return
     */
    DataRet<Order> findById(Long orderId);

    /**
     * 确认收货
     *
     * @param orderId
     * @return
     */
    DataRet<String> confirmByGood(Long orderId, String ipAddress);

    /**
     * 取消订单
     *
     * @param orderId
     * @param ipAddress
     * @return
     */
    DataRet<String> cancelByOrder(Long orderId, String ipAddress);

    /**
     * 新增订单
     * @param order
     * @return
     */
    DataRet<String> addOrder(Order order);
}

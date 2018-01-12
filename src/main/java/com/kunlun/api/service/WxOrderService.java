package com.kunlun.api.service;

import com.kunlun.entity.Order;
import com.kunlun.entity.OrderExt;
import com.kunlun.result.DataRet;
import com.kunlun.result.PageResult;
import com.kunlun.wxentity.OrderCondition;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    DataRet<String> applyRefund(Long orderId);

    /**
     * 查询订单详情
     *
     * @param orderId
     * @return
     */
    DataRet<OrderExt> findById(Long orderId);

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
     *
     * @param order
     * @return
     */
    DataRet<String> addOrder(Order order);

    /**
     * 修改订单预付款订单号
     *
     * @param id
     * @param prepayId
     * @return
     */
    DataRet<String> updateOrderPrepayId(Long id, String prepayId);


    /**
     * 查询未付款订单列表
     *
     * @return List<Order>
     */
    DataRet<List<Order>> findUnPayOrder(String orderStatus);

    /**
     * 查询退款中的订单列表
     *
     * @param orderStatus
     * @return
     */
    DataRet<List<Order>> findRefundingOrder(String orderStatus);

}

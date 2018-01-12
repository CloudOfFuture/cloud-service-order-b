package com.kunlun.api.service;

import com.kunlun.entity.Order;
import com.kunlun.result.DataRet;
import com.kunlun.result.PageResult;
import com.kunlun.wxentity.OrderCondition;

import java.util.List;

/**
 * @author by kunlun
 * @version <0.1>
 * @created on 2017/12/20.
 */
public interface OrderService {
    /**
     * @param orderNo   订单号
     * @param phone     收件人手机
     * @param status    订单状态
     * @param orderType 订单类型
     * @param searchKey 搜索关键字
     * @param pageNo    当前页
     * @param pageSize  每页条数
     * @return
     */
    PageResult list(String orderNo,
                    String phone,
                    String status,
                    String orderType,
                    String searchKey,
                    Integer pageNo,
                    Integer pageSize);

    /**
     * 发货
     *
     * @param orderCondition
     * @return
     */
    DataRet<String> sendGood(OrderCondition orderCondition);

    /**
     * 修改订单
     *
     * @param order
     * @return
     */
    DataRet<String> modify(Order order);

    /**
     * 订单详情
     *
     * @param orderId
     * @return
     */
    DataRet<Order> findById(Long orderId, Long sellerId);

    /**
     * 退款审核
     *
     * @param orderId
     * @param flag      AGREE 同意  REFUSE  拒绝
     * @param remark
     * @param refundFee
     * @return
     */
    DataRet<String> auditRefund(Long orderId, String flag, String remark, Integer refundFee);

    /**
     * 修改订单装态
     *
     * @param id
     * @param status
     * @return
     */
    DataRet<String> updateOrderStatus(Long id, String status);

    /**
     * 根据订单号查找订单
     *
     * @param orderNo
     * @return
     */
    DataRet<Order> findByOrderNo(String orderNo);

    /**
     * 修改微信订单状态和微信支付订单号
     *
     * @param id
     * @param status
     * @param wxOrderNo
     * @return
     */
    DataRet<String> modifyStatusAndWxOrderNo(Long id, String status, String wxOrderNo);

}

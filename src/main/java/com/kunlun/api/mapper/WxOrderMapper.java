package com.kunlun.api.mapper;

import com.github.pagehelper.Page;
import com.kunlun.entity.Order;
import com.kunlun.entity.OrderExt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author by kunlun
 * @version <0.1>
 * @created on 2017/12/20.
 */
@Mapper
public interface WxOrderMapper {

    /**
     * 订单列表
     *
     * @param userId
     * @param orderStatus
     * @param payType
     * @return
     */
    Page<Order> findByOpenid(@Param("userId") String userId,
                             @Param("orderStatus") String orderStatus,
                             @Param("payType") String payType);

    /**
     * 查询订单详情
     *
     * @param orderId
     * @return
     */
    OrderExt findById(@Param("orderId") Long orderId);

    /**
     * 申请退款
     *
     * @param orderId
     * @return
     */
    int applyRefund(@Param("orderId") Long orderId);

    /**
     * 更新订单状态
     *
     * @param orderId
     * @param orderStatus
     * @return
     */
    int updateOrderStatus(@Param("orderId") Long orderId, @Param("orderStatus") String orderStatus);


    /**
     * 新增订单
     *
     * @param order
     * @return
     */
    int addOrder(Order order);

    /**
     * 修改订单预付款订单号
     *
     * @param id
     * @param prepayId
     * @return
     */
    int updatePrepayId(@Param("id") Long id, @Param("prepayId") String prepayId);

    /**
     * 查询退款中的订单列表
     *
     * @param orderStatus
     * @return
     */
    List<Order> findRefundingOrder(@Param("orderStatus") String orderStatus);

    /**
     * 查询未付款订单列表
     *
     * @param orderStatus
     * @return
     */
    List<Order> findUnPayOrder(@Param("orderStatus") String orderStatus);
}

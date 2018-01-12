package com.kunlun.api.mapper;

import com.github.pagehelper.Page;
import com.kunlun.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author by kunlun
 * @version <0.1>
 * @created on 2017/12/20.
 */
@Mapper
public interface OrderMapper {

    /**
     * 订单列表
     *
     * @param orderNo
     * @param phone
     * @param status
     * @param orderType
     * @param searchKey
     * @return
     */
    Page<Order> list(@Param("orderNo") String orderNo,
                     @Param("phone") String phone,
                     @Param("status") String status,
                     @Param("orderType") String orderType,
                     @Param("searchKey") String searchKey);

    /**
     * 根据id查询订单
     *
     * @param orderId
     * @param sellerId
     * @param orderStatus
     * @return
     */
    Order findByOrderIdAndSellerId(@Param("orderId") Long orderId,
                                   @Param("sellerId") Long sellerId,
                                   @Param("orderStatus") String orderStatus);

    /**
     * 根据订单id更新订单状态 以及发货信息
     *
     * @param orderId
     * @param status
     * @param logisticId
     * @param logisticNo
     * @return
     */
    int updateOrderStatus(@Param("orderId") Long orderId,
                          @Param("status") String status,
                          @Param("logisticNo") String logisticNo,
                          @Param("logisticId") Long logisticId);

    /**
     * 修改订单
     *
     * @param orderId
     * @param province
     * @param city
     * @param area
     * @param address
     * @param remark
     * @param phone
     * @return
     */

    int modify(@Param("orderId") Long orderId,
               @Param("province") String province,
               @Param("city") String city,
               @Param("area") String area,
               @Param("address") String address,
               @Param("remark") String remark,
               @Param("phone") String phone);


    /**
     * 退款
     *
     * @param order
     * @return
     */
    int refund(Order order);

    /**
     * 修改订单状态
     *
     * @param id
     * @param status
     * @return
     */
    int updateOrderStatus(@Param("id") Long id, @Param("status") String status);


    /**
     * 根据微信订单号查找订单
     *
     * @param orderNo
     * @return
     */
    Order findOrderByOrderNo(@Param("orderNo") String orderNo);

    /**
     * 修改订单状态和微信支付订单号
     *
     * @param id
     * @param orderStatus
     * @param wxOrderNo
     * @return
     */
    int modifyOrderStatusAndWxOrderNo(@Param("id") Long id,
                                      @Param("orderStatus") String orderStatus,
                                      @Param("wxOrderNo") String wxOrderNo);

    /**
     * 根据id查询详情
     *
     * @param orderId
     * @return
     */
    Order findById(@Param("orderId") Long orderId);

    /**
     * 退款审核
     *
     * @param orderId
     * @param orderStatus
     * @param refundFee
     * @param remark
     * @return int
     */
    int auditRefund(@Param("orderId") Long orderId,
                    @Param("orderStatus") String orderStatus,
                    @Param("refundFee") Integer refundFee,
                    @Param("remark") String remark);
}

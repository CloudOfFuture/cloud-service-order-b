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
     * @param type
     * @param searchKey
     * @return
     */
    Page<Order> list(@Param("orderNo") String orderNo,
                     @Param("phone") String phone,
                     @Param("status") String status,
                     @Param("type") String type,
                     @Param("searchKey") String searchKey);

    /**
     * 根据id查询订单
     *
     * @param orderId
     * @param sellerId
     * @param status
     * @return
     */
    Order findByOrderIdAndSellerId(@Param("orderId") Long orderId,
                                   @Param("sellerId") Long sellerId,
                                   @Param("status") String status);

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


}

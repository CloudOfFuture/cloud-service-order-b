package com.kunlun.api.controller;

import com.kunlun.api.service.OrderService;
import com.kunlun.entity.Order;
import com.kunlun.result.DataRet;
import com.kunlun.result.PageResult;
import com.kunlun.wxentity.OrderCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author by kunlun
 * @version <0.1>
 * @created on 2017/12/20.
 */
@RestController
@RequestMapping("backend/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 订单列表
     *
     * @param orderNo
     * @param phone
     * @param status
     * @param orderType
     * @param searchKey
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping("/findByCondition")
    public PageResult list(@RequestParam(value = "orderNo", required = false) String orderNo,
                           @RequestParam(value = "phone", required = false) String phone,
                           @RequestParam(value = "status", required = false) String status,
                           @RequestParam(value = "orderType", required = false) String orderType,
                           @RequestParam(value = "searchKey", required = false) String searchKey,
                           @RequestParam(value = "pageNo") Integer pageNo,
                           @RequestParam(value = "pageSize") Integer pageSize) {
        return orderService.list(orderNo, phone, status, orderType, searchKey, pageNo, pageSize);
    }

    /**
     * 发货
     *
     * @param orderCondition
     * @return
     */
    @PostMapping("/sendGood")
    public DataRet<String> sendGood(@RequestBody OrderCondition orderCondition) {
        return orderService.sendGood(orderCondition);
    }

    /**
     * 修改订单
     *
     * @param order
     * @return
     */
    @PostMapping("/modify")
    public DataRet<String> modify(@RequestBody Order order) {
        return orderService.modify(order);
    }

    /**
     * 订单详情
     *
     * @param orderId
     * @param sellerId
     * @return
     */
    @GetMapping("/findById")
    public DataRet<Order> findById(@RequestParam(value = "orderId") Long orderId,
                                   @RequestHeader(value = "sellerId") Long sellerId) {
        return orderService.findById(orderId, sellerId);
    }

    /**
     * 根据订单编号查找订单
     *
     * @param orderNo
     * @return
     */
    @GetMapping("/findByOrderNo")
    public DataRet<Order> findByOrderNo(@RequestParam(value = "orderNo") String orderNo) {
        return orderService.findByOrderNo(orderNo);
    }

    /**
     * 退款
     *
     * @param orderId   Long
     * @param flag      AGREE 同意  REFUSE  拒绝
     * @param remark    String
     * @param refundFee Integer
     * @return DataRet
     */
    @PostMapping("/audit/refund")
    public DataRet<String> auditRefund(@RequestParam("orderId") Long orderId,
                                       @RequestParam("flag") String flag,
                                       @RequestParam(value = "remark", required = false) String remark,
                                       @RequestParam(value = "refundFee") Integer refundFee) {
        return orderService.auditRefund(orderId, flag, remark, refundFee);
    }

    /**
     * 修改订单状态
     *
     * @param id
     * @param status
     * @return
     */
    @PostMapping("/updateStatusById")
    public DataRet<String> updateOrderStatusById(@RequestParam(value = "id") Long id,
                                                 @RequestParam(value = "status") String status) {
        return orderService.updateOrderStatus(id, status);
    }

    @PostMapping("/modifyStatusAndPayOrderNo")
    public DataRet<String> modifyStatusAndPayOrderNo(@RequestParam(value = "id") Long id,
                                                     @RequestParam(value = "status") String status,
                                                     @RequestParam(value = "wxOrderNo") String wxOrderNo) {
        return orderService.modifyStatusAndWxOrderNo(id, status, wxOrderNo);
    }
}

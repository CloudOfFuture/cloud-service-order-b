package com.kunlun.api.controller;

import com.kunlun.api.service.WxOrderService;
import com.kunlun.entity.Estimate;
import com.kunlun.entity.Order;
import com.kunlun.entity.OrderExt;
import com.kunlun.result.DataRet;
import com.kunlun.result.PageResult;
import com.kunlun.utils.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author by kunlun
 * @version <0.1>
 * @created on 2017/12/20.
 */
@RestController
@RequestMapping("/wx/order")
public class WxOrderController {

    @Autowired
    private WxOrderService wxOrderService;

    /**
     * 我的订单列表/   分类查询（订单状态/支付类型）
     *
     * @param pageNo      当前页
     * @param pageSize    每条页数
     * @param wxCode      微信code
     * @param orderStatus 订单状态
     * @param payType     支付类型
     * @return
     */
    @GetMapping("/findByOpenid")
    public PageResult findByOpenid(@RequestParam(value = "pageNo") Integer pageNo,
                                   @RequestParam(value = "pageSize") Integer pageSize,
                                   @RequestParam(value = "wxCode") String wxCode,
                                   @RequestParam(value = "orderStatus", required = false) String orderStatus,
                                   @RequestParam(value = "payType", required = false) String payType) {
        return wxOrderService.findByOpenid(pageNo, pageSize, wxCode, orderStatus, payType);
    }

    /**
     * 申请退款/退款金额
     *
     * @param orderId 订单id
     * @return
     */
    @GetMapping("/applyRefund")
    public DataRet<String> applyRefund(@RequestParam(value = "orderId") Long orderId) {
        return wxOrderService.applyRefund(orderId);
    }

    /**
     * 查询订单详情
     *
     * @param orderId 订单id
     * @return
     */
    @GetMapping("/findById")
    public DataRet<OrderExt> findById(@RequestParam(value = "orderId") Long orderId) {
        return wxOrderService.findById(orderId);
    }

    /**
     * 确认收货
     *
     * @param orderId   订单id
     * @param ipAddress 请求ip
     * @return
     */
    @PostMapping("/confirmByGood")
    public DataRet<String> confirmByGood(@RequestParam(value = "orderId") Long orderId,
                                         @RequestParam(value = "ipAddress", required = false) String ipAddress) {
        return wxOrderService.confirmByGood(orderId, ipAddress);
    }

    /**
     * 取消订单
     *
     * @param orderId   订单id
     * @param ipAddress 请求ip
     * @return
     */
    @PostMapping("/cancelByOrder")
    public DataRet<String> cancelByOrder(@RequestParam(value = "orderId") Long orderId,
                                         @RequestParam(value = "ipAddress", required = false) String ipAddress) {
        return wxOrderService.cancelByOrder(orderId, ipAddress);
    }

    /**
     * 新增订单
     *
     * @param order
     * @return
     */
    @PostMapping("/addOrder")
    public DataRet<String> addOrder(@RequestBody Order order) {
        return wxOrderService.addOrder(order);
    }

    /**
     * 修改订单预付款订单号
     *
     * @param id
     * @param prepayId
     * @return
     */
    @PostMapping("/updatePrepayId")
    public DataRet<String> updateOrderPrepayId(@RequestParam(value = "id") Long id,
                                               @RequestParam(value = "prepayId") String prepayId) {
        return wxOrderService.updateOrderPrepayId(id, prepayId);
    }

    /**
     * 查询退款中的订单列表
     *
     * @param orderStatus 订单状态
     * @return
     */
    @GetMapping("/findRefundingOrder")
    public DataRet<List<Order>> findRefundingOrder(@RequestParam(value = "orderStatus") String orderStatus) {
        return wxOrderService.findRefundingOrder(orderStatus);
    }

    /**
     * 查询未付款订单列表
     *
     * @param orderStatus
     * @return
     */
    @GetMapping("/findUnPayOrder")
    public DataRet<List<Order>> findUnPayOrder(@RequestParam(value = "orderStatus") String orderStatus) {
        return wxOrderService.findUnPayOrder(orderStatus);
    }
}

package com.kunlun.api.controller;

import com.kunlun.api.service.WxOrderService;
import com.kunlun.entity.Order;
import com.kunlun.result.DataRet;
import com.kunlun.result.PageResult;
import com.kunlun.utils.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
     * @param orderId   订单id
     * @return
     */
    @GetMapping("/refund")
    public DataRet<String> refund(@RequestParam(value = "order_id") Long orderId) {
        return wxOrderService.refund(orderId);
    }

    /**
     * 查询订单详情
     *
     * @param orderId 订单id
     * @return
     */
    @GetMapping("/findById")
    public DataRet<Order> findById(@RequestParam(value = "order_id") Long orderId) {
        return wxOrderService.findById(orderId);
    }

    //TODO 签收后评价（cloud-service-common）

    /**
     * 确认收货
     *
     * @param orderId 订单id
     * @param request 请求ip
     * @return
     */
    @GetMapping("/confirmByGood")
    public DataRet<String> confirmByGood(@RequestParam(value = "order_id") Long orderId,
                                         HttpServletRequest request) {
        String ipAddress = IpUtil.getIPAddress(request);
        return wxOrderService.confirmByGood(orderId, ipAddress);
    }

    /**
     * 取消订单
     *
     * @param orderId 订单id
     * @param request 请求ip
     * @return
     */
    @GetMapping("/cancelByOrder")
    public DataRet<String> cancelByOrder(@RequestParam(value = "order_id") Long orderId,
                                         HttpServletRequest request) {
        String ipAddress = IpUtil.getIPAddress(request);
        return wxOrderService.cancelByOrder(orderId, ipAddress);
    }

    /**
     * 新增订单
     * @param order
     * @return
     */
    @PostMapping("/addOrder")
    public DataRet<String> addOrder(@RequestBody Order order){
        return wxOrderService.addOrder(order);
    }

}

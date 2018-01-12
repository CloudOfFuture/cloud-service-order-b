package com.kunlun.api.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kunlun.api.client.DeliveryClient;
import com.kunlun.api.client.LogClient;
import com.kunlun.api.mapper.WxOrderMapper;
import com.kunlun.entity.Delivery;
import com.kunlun.entity.Order;
import com.kunlun.entity.OrderExt;
import com.kunlun.entity.OrderLog;
import com.kunlun.enums.CommonEnum;
import com.kunlun.result.DataRet;
import com.kunlun.result.PageResult;
import com.kunlun.utils.CommonUtil;
import com.kunlun.utils.WxUtil;
import com.mysql.jdbc.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author by kunlun
 * @version <0.1>
 * @created on 2017/12/20.
 */
@Service
public class WxOrderServiceImpl implements WxOrderService {

    @Autowired
    private WxOrderMapper wxOrderMapper;

    @Autowired
    private DeliveryClient deliveryClient;

    @Autowired
    private LogClient logClient;

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
    @Override
    public PageResult findByOpenid(Integer pageNo, Integer pageSize, String wxCode, String orderStatus, String payType) {
        String userId = WxUtil.getOpenId(wxCode);
        PageHelper.startPage(pageNo, pageSize);
        if (StringUtils.isNullOrEmpty(wxCode)) {
            return new PageResult("ERROR", "微信code为空");
        }
        if (CommonEnum.ALL.getCode().equals(orderStatus)) {
            orderStatus = null;
        }
        Page<Order> page = wxOrderMapper.findByOpenid(userId, orderStatus, payType);
        return new PageResult<>(page);
    }

    /**
     * 申请退款
     *
     * @param orderId
     * @return
     */
    @Override
    public DataRet<String> applyRefund(Long orderId) {
        Order order = wxOrderMapper.findById(orderId);
        if (!order.getOrderStatus().equals(CommonEnum.UN_DELIVERY.getCode())) {
            return new DataRet<>("ERROR", "订单状态不对");
        }
        int result = wxOrderMapper.applyRefund(orderId);
        if (result > 0) {
            return new DataRet<>("申请退款成功");
        }
        return new DataRet<>("ERROR", "申请退款失败");
    }

    /**
     * 查询订单详情
     *
     * @param orderId
     * @return
     */
    @Override
    public DataRet<OrderExt> findById(Long orderId) {
        if (orderId == null) {
            return new DataRet<>("ERROR", "传入的参数有误");
        }
        OrderExt orderExt = wxOrderMapper.findById(orderId);
        if (orderExt == null) {
            return new DataRet<>("ERROR", "订单不存在");
        }
        //订单收货地址
        DataRet<Delivery> deliveryRet = deliveryClient.findById(orderExt.getDeliveryId());
        orderExt.setDelivery(deliveryRet.getBody());
        //TODO 订单发货信息
        return new DataRet<>(orderExt);
    }

    /**
     * 确认收货
     *
     * @param orderId
     * @return
     */
    @Override
    public DataRet<String> confirmByGood(Long orderId, String ipAddress) {
        if (orderId == null) {
            return new DataRet<>("ERROR", "传入的参数有误");
        }
        //查询订单详情
        Order order = wxOrderMapper.findById(orderId);
        if (order == null) {
            return new DataRet<>("ERROR", "订单不存在");
        }
        //判断/更改订单状态(已完成）
        boolean flag = !CommonEnum.UN_RECEIVE.getCode().equals(order.getOrderStatus());
        if (flag) {
            return new DataRet<>("ERROR", "订单状态不对，不能确认收货");
        }
        int result = wxOrderMapper.updateOrderStatus(orderId, CommonEnum.DONE.getCode());
        if (result > 0) {
            saveOrderLog(orderId, order.getOrderNo(), "确认收货");
            return new DataRet<>("确认收货成功");
        }
        return new DataRet<>("ERROR", "确认收货失败");
    }


    /**
     * 取消订单
     *
     * @param orderId
     * @param ipAddress
     * @return
     */
    @Override
    public DataRet<String> cancelByOrder(Long orderId, String ipAddress) {
        if (orderId == null) {
            return new DataRet<>("ERROR", "传入的参数有误");
        }
        //查询订单详情
        Order order = wxOrderMapper.findById(orderId);
        if (order == null) {
            return new DataRet<>("ERROR", "订单不存在");
        }
        //关闭订单
        if (!CommonEnum.UN_PAY.getCode().equals(order.getOrderStatus())) {
            return new DataRet<>("ERROR", "订单状态异常，不能取消订单");
        }
        int result = wxOrderMapper.updateOrderStatus(orderId, CommonEnum.CLOSING.getCode());
        if (result > 0) {
            saveOrderLog(orderId, order.getOrderNo(), "取消订单成功");
            return new DataRet<>("取消订单成功");
        }
        return new DataRet<>("ERROR", "取消订单失败");
    }

    /**
     * 新增订单
     *
     * @param order
     * @return
     */
    @Override
    public DataRet<String> addOrder(Order order) {
        Integer res = wxOrderMapper.addOrder(order);
        if (res > 0) {
            return new DataRet<>("下单成功");
        }
        return new DataRet<>("ERROR", "下单失败");
    }

    /**
     * 修改订单预付款订单号
     *
     * @param id
     * @param prepayId
     * @return
     */
    @Override
    public DataRet<String> updateOrderPrepayId(Long id, String prepayId) {
        Integer result = wxOrderMapper.updatePrepayId(id, prepayId);
        if (result > 0) {
            return new DataRet<>("修改成功");
        }
        return new DataRet<>("ERROR", "修改预付款订单号失败");
    }

    /**
     * 查询未付款订单列表
     *
     * @param orderStatus
     * @return
     */
    @Override
    public DataRet<List<Order>> findUnPayOrder(String orderStatus) {
        List<Order> result = wxOrderMapper.findUnPayOrder(orderStatus);
        if (result == null || result.size() == 0) {
            return new DataRet<>("ERROR", "暂无数据");
        }
        return new DataRet<>(result);
    }

    /**
     * 查询退款中的订单列表
     *
     * @param orderStatus
     * @return
     */
    @Override
    public DataRet<List<Order>> findRefundingOrder(String orderStatus) {
        List<Order> result = wxOrderMapper.findRefundingOrder(orderStatus);
        if (result == null || result.size() == 0) {
            return new DataRet<>("ERROR", "暂无数据");
        }
        return new DataRet<>(result);
    }

    /**
     * 保存订单日志
     *
     * @param orderId
     * @param orderNo
     * @param action
     */
    private void saveOrderLog(Long orderId, String orderNo, String action) {
        OrderLog orderLog = CommonUtil.constructOrderLog(orderNo,
                action, null, orderId);
        logClient.addOrderLog(orderLog);
    }
}

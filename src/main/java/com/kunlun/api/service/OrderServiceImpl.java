package com.kunlun.api.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kunlun.api.mapper.OrderMapper;
import com.kunlun.entity.Order;
import com.kunlun.enums.CommonEnum;
import com.kunlun.result.DataRet;
import com.kunlun.result.PageResult;
import com.kunlun.wxentity.OrderCondition;
import com.mysql.jdbc.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author by kunlun
 * @version <0.1>
 * @created on 2017/12/20.
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * @param orderNo   订单号
     * @param phone     收件人手机
     * @param status    订单状态
     * @param type      订单类型
     * @param searchKey 搜索关键字
     * @param pageNo    当前页
     * @param pageSize  每页条数
     * @return
     */
    @Override
    public PageResult list(String orderNo, String phone, String status, String type, String searchKey, Integer pageNo, Integer pageSize) {
        if (StringUtils.isNullOrEmpty(String.valueOf(pageNo)) ||
                StringUtils.isNullOrEmpty(String.valueOf(pageSize))) {
            return new PageResult("ERROR", "参数错误");
        }
        PageHelper.startPage(pageNo, pageSize);
        Page<Order> page = orderMapper.list(orderNo, phone, status, type, searchKey);
        return new PageResult(page);
    }

    /**
     * 发货
     *
     * @param orderCondition
     * @return
     */
    @Override
    public DataRet<String> sendGood(OrderCondition orderCondition) {
        Long orderId = orderCondition.getOrderId();
        /**
         * 根据 订单id、店铺id、订单状态为待发货查询订单
         */
        Order order = orderMapper.findByOrderIdAndSellerId(orderId, orderCondition.getSellerId(),
                CommonEnum.UN_DELIVERY.getCode());
        if (null == order) {
            return new DataRet<>("ERROR", "订单不存在");
        }

        return new DataRet<>(order.getOrderNo());
    }

    /**
     * 修改订单
     *
     * @param order
     * @return
     */
    @Override
    public DataRet<String> modify(Order order) {
        Order result = orderMapper.findByOrderIdAndSellerId(order.getId(), order.getSellerId(),
                CommonEnum.UN_DELIVERY.getCode());
        if (null == result) {
            return new DataRet<>("ERROR", "订单不存在");
        }
        orderMapper.modify(order.getId(),
                order.getProvince(),
                order.getCity(),
                order.getArea(),
                order.getAddress(),
                order.getRemark(),
                order.getPhone());
        return new DataRet<>("订单修改成功");
    }

    /**
     * 订单详情
     *
     * @param orderId
     * @param sellerId
     * @return
     */
    @Override
    public DataRet<Order> findById(Long orderId, Long sellerId) {
        Order order = orderMapper.findByOrderIdAndSellerId(orderId, sellerId, null);
        if (null == order) {
            return new DataRet<>("ERROR", "订单不存在");
        }
        return new DataRet<>(order);
    }

    /**
     * 退款
     *
     * @param order
     * @return
     */
    @Override
    public DataRet<String> refund(Order order) {
        int result = orderMapper.refund(order);
        if (result > 0) {
            return new DataRet<>("退款成功");
        }
        return new DataRet<>("ERROR", "退款失败");
    }


}

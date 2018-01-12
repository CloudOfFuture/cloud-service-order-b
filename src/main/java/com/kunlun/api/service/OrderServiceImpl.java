package com.kunlun.api.service;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kunlun.api.client.GoodClient;
import com.kunlun.api.client.LogClient;
import com.kunlun.api.client.PointClient;
import com.kunlun.api.mapper.OrderMapper;
import com.kunlun.entity.*;
import com.kunlun.enums.CommonEnum;
import com.kunlun.result.DataRet;
import com.kunlun.result.PageResult;
import com.kunlun.utils.CommonUtil;
import com.kunlun.wxentity.OrderCondition;
import com.mysql.jdbc.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by kunlun
 * @version <0.1>
 * @created on 2017/12/20.
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private LogClient logClient;

    @Autowired
    private PointClient pointClient;

    @Autowired
    private GoodClient goodClient;

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
    @Override
    public PageResult list(String orderNo, String phone, String status, String orderType, String searchKey, Integer pageNo, Integer pageSize) {
        if (StringUtils.isNullOrEmpty(String.valueOf(pageNo)) ||
                StringUtils.isNullOrEmpty(String.valueOf(pageSize))) {
            return new PageResult("ERROR", "参数错误");
        }
        PageHelper.startPage(pageNo, pageSize);
        if (!StringUtils.isNullOrEmpty(searchKey)) {
            searchKey = "%" + searchKey + "%";
        }
        Page<Order> page = orderMapper.list(orderNo, phone, status, orderType, searchKey);
        return new PageResult<>(page);
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
        Integer result = orderMapper.modifyOrderStatusAndWxOrderNo(orderId, CommonEnum.UN_RECEIVE.getCode(), null);
        if (result < 0) {
            return new DataRet<>("ERROR", "发货成功，但未修改订单状态");
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
     * @param orderId
     * @param flag      AGREE 同意  REFUSE  拒绝
     * @param remark
     * @param refundFee
     * @return
     */
    @Override
    public DataRet<String> auditRefund(Long orderId, String flag, String remark, Integer refundFee) {
        int result;
        Order order = orderMapper.findById(orderId);
        if (CommonEnum.REFUSE.getCode().equals(flag)) {
            result = orderMapper.auditRefund(orderId, CommonEnum.REFUND_FAIL.getCode(), null, remark);
            saveOrderLog(orderId, order.getOrderNo(), remark);
        } else {
            List<PointLog> pointLogList = new ArrayList<>();
            result = orderMapper.auditRefund(orderId, CommonEnum.REFUNDING.getCode(), refundFee, remark);
            if (result == 0) {
                return new DataRet<>("审核成功");
            }
            saveOrderLog(order.getId(), order.getOrderNo(), "退款");
            DataRet updateStockResult = goodClient.updateStock(order.getGoodId(), order.getCount());
            if (!updateStockResult.isSuccess()) {
                return new DataRet<>("ERROR", "审核失败");
            }
            //记录商品库存反还日志
            saveGoodLog(order.getGoodName(), order.getGoodId(), "退款，商品库存返还");
            //使用积分返还
            //支付时扣减的积分，需要返还
            int operatePoint = order.getOperatePoint();
            DataRet<Point> dataRet = pointClient.findByUserId(order.getUserId());
            if (!dataRet.isSuccess()) {
                return new DataRet<>("ERROR", "审核失败");
            }
            Point localPoint = dataRet.getBody();
            if (CommonEnum.USE_POINT.getCode().equals(order.getUsePoint())) {
                //积分返还
                pointClient.updatePoint(order.getUserId(), operatePoint);
                //记录积分返还日志
                localPoint.setPoint(localPoint.getPoint() + operatePoint);
                pointLogList.add(CommonUtil.constructPointLog(order.getUserId(),
                        +operatePoint, localPoint.getPoint() + operatePoint));
            }
            //支付产生的积分，需要扣减
            int point2 = order.getPaymentFee();
            int currPoint = localPoint.getPoint() - point2;
            //积分返还
            pointClient.updatePoint(order.getUserId(), -point2);
            //积分扣减日志
            pointLogList.add(CommonUtil.constructPointLog(order.getUserId(), -point2, currPoint));
            savePointLog(pointLogList);
        }
        if (result > 0) {
            return new DataRet<>("审核成功");
        }
        return new DataRet<>("ERROR", "审核失败");
    }

    private void savePointLog(List<PointLog> pointLogList) {
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(pointLogList);
        logClient.addPointLogList(jsonArray);
    }

    private void saveOrderLog(Long id, String orderNo, String action) {
        logClient.addOrderLog(CommonUtil.constructOrderLog(orderNo, action, null, id));
    }

    private void saveGoodLog(String goodName, Long goodId, String action) {
        logClient.addGoodLog(CommonUtil.constructGoodLog(goodId, goodName, action));
    }

    /**
     * 修改订单装态
     *
     * @param id
     * @param status
     * @return
     */
    @Override
    public DataRet<String> updateOrderStatus(Long id, String status) {
        Integer result = orderMapper.updateOrderStatus(id, status);
        if (result <= 0) {
            return new DataRet<>("ERROR", "修改订单状态失败");
        }
        return new DataRet<>("修改状态成功");
    }

    /**
     * 根据订单号查找订单
     *
     * @param orderNo
     * @return
     */
    @Override
    public DataRet<Order> findByOrderNo(String orderNo) {
        Order order = orderMapper.findOrderByOrderNo(orderNo);
        if (order == null) {
            return new DataRet<>("ERROR", "查无此订单");
        }
        return new DataRet<>(order);
    }

    /**
     * 修改微信订单状态和微信支付订单号
     *
     * @param id
     * @param status
     * @param wxOrderNo
     * @return
     */
    @Override
    public DataRet<String> modifyStatusAndWxOrderNo(Long id, String status, String wxOrderNo) {
        Integer result = orderMapper.modifyOrderStatusAndWxOrderNo(id, status, wxOrderNo);
        if (result <= 0) {
            return new DataRet<>("ERROR", "修改订单状态失败");
        }
        return new DataRet<>("操作成功");
    }
}

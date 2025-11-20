package com.qiguliuxing.dts.wx.web;

import com.qiguliuxing.dts.core.util.ResponseUtil;
import com.qiguliuxing.dts.db.domain.DtsOrder;
import com.qiguliuxing.dts.db.service.DtsOrderService;
import com.qiguliuxing.dts.db.util.OrderUtil;
import com.qiguliuxing.dts.wx.service.WxOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 模拟支付控制器（仅在开发环境启用）
 */
@ConditionalOnProperty(name = "wx.mock-pay", havingValue = "true")
@RestController
@RequestMapping("/order")
public class MockOrderController {

    @Autowired
    private DtsOrderService orderService;
    
    @Autowired
    private WxOrderService wxOrderService;

    /**
     * 模拟支付接口
     * @param body 请求体 { orderId: xxx }
     * @return 支付结果
     */
    @PostMapping("/mockPay")
    public Object mockPay(@RequestBody Map<String, Object> body) {
        Integer orderId = (Integer) body.get("orderId");
        if (orderId == null) {
            return ResponseUtil.badArgument();
        }

        // 1. 获取订单
        DtsOrder order = orderService.findById(orderId);
        if (order == null) {
            return ResponseUtil.fail(720, "订单不存在");
        }

        // 2. 验证订单状态
        if (!OrderUtil.STATUS_CREATE.equals(order.getOrderStatus())) {
            return ResponseUtil.fail(725, "订单当前状态不能支付");
        }

        // 3. 模拟支付处理
        order.setPayId("MOCK_" + System.currentTimeMillis());
        order.setPayTime(LocalDateTime.now());
        order.setOrderStatus(OrderUtil.STATUS_PAY);
//        order.setPayType(1); // 标记为模拟支付

        // 4. 更新订单
        if (orderService.updateWithOptimisticLocker(order) == 0) {
            return ResponseUtil.updatedDateExpired();
        }

        // 5. 返回成功
        return ResponseUtil.ok();
    }

    /**
     * 模拟支付带完整流程（包含通知）
     */
    @PostMapping("/mockPayWithNotify")
    public Object mockPayWithNotify(@RequestBody Map<String, Object> body) {
        // 直接调用主服务的方法
        return wxOrderService.mockPay(
            (Integer) body.get("userId"),
            (Integer) body.get("orderId")
        );
    }
}
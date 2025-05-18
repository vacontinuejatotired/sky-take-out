package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrdersMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class OrderTask {
    @Autowired
    private OrdersMapper ordersMapper;


    @Scheduled(cron = "0 * * * * ? ")
    public void processTimeOutOrder() {
        log.info("OrderTask processTimeOrder");
        int status= Orders.PENDING_PAYMENT;
        LocalDateTime now = LocalDateTime.now();
        now=now.plusMinutes(-15);
        List<Orders>list=ordersMapper.getByStatusAndTimeLt(status,now);
        if (list!=null&&list.size()>0) {
            for(Orders orders:list){
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelReason("订单超时，自动取消");
                orders.setCancelTime(LocalDateTime.now());
                ordersMapper.update(orders);
            }
        }
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void processDeliveryOrder() {
    log.info("定时处理派送中的订单,{}",LocalDateTime.now());
    int status=Orders.DELIVERY_IN_PROGRESS;
    LocalDateTime now = LocalDateTime.now();
    now=now.plusMinutes(-60);
    List<Orders>list=ordersMapper.getByStatusAndTimeLt(status,now);
    if (list!=null&&list.size()>0) {
        for(Orders orders:list){
            orders.setStatus(Orders.COMPLETED);
            ordersMapper.update(orders);
        }
    }
    }
}

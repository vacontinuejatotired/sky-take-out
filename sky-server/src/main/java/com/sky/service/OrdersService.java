package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrdersService {
    public OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO);

    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    void paySuccess(String outTradeNo);

    PageResult pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    OrderVO selectOrderDetail(Long id);

    void cancelById(Long id);

    void submitAgain(Long id);

    OrderStatisticsVO getStatisticsVo();

    PageResult searchList(OrdersPageQueryDTO ordersPageQueryDTO);

    void confirm(Long id);

    void rejectOrder(OrdersRejectionDTO ordersRejectionDTO);
}

package com.sky.service;

import com.sky.dto.OrdersSubmitDTO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrdersService {
    public OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO);
}

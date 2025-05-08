package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.dto.OrdersDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.Result;
import com.sky.service.OrdersService;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Api("订单接口")
@Slf4j
@RestController("userOrderController")
@RequestMapping("/user/order")
public class OrderController {
    @Autowired
    private OrdersService ordersService;
    @ApiOperation("下单")
    @PostMapping("/submit")
    public Result submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("用户{}下单", BaseContext.getCurrentId());
        OrderSubmitVO orderSubmitVO=ordersService.submit(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }
}

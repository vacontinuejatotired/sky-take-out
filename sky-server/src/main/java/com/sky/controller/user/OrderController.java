package com.sky.controller.user;

import com.sky.dto.OrdersDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.Result;
import com.sky.service.OrdersService;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Api("订单接口")
@Slf4j
@RestController("orderController")
@RequestMapping("/user/order")
public class OrderController {
    @Autowired
    private OrdersService ordersService;
    @ApiOperation("")
    @PutMapping("/submit")
    public Result submit(@RequestBody OrdersDTO ordersDto) {

        return Result.success();
    }
}

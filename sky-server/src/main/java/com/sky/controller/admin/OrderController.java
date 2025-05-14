package com.sky.controller.admin;

import com.sky.context.BaseContext;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.OrdersDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrdersService;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("adminOrderController")
@Slf4j
@RequestMapping("/admin/order")
@Api("管理端订单接口")
public class OrderController {
    @Autowired
    private OrdersService ordersService;

    @GetMapping("/conditionSearch")
    @ApiOperation("订单搜索")
    public Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("admin搜索订单{}",ordersPageQueryDTO);
        PageResult pageResult=ordersService.searchList(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    @ApiOperation("各个状态的订单展示")
    @GetMapping("/statistics")
    public Result<OrderStatisticsVO> statistics() {
        OrderStatisticsVO orderStatisticsVO=new OrderStatisticsVO();
        orderStatisticsVO=ordersService.getStatisticsVo();
        return Result.success(orderStatisticsVO);
    }

    @ApiOperation("查询订单详细")
    @GetMapping("/details/{id}")
    public Result<OrderVO> details(@PathVariable("id") Long id) {
        log.info("商家{}查询订单详细", BaseContext.getCurrentId());
        OrderVO orderVO=ordersService.selectOrderDetail(id);
        return Result.success(orderVO);
    }

    @ApiOperation("接单")
    @PutMapping("/confirm")
    public Result confirm(@RequestBody OrdersDTO ordersDTO) {
        log.info("商家{}接单", BaseContext.getCurrentId());
        ordersService.confirm(ordersDTO.getId());
        return Result.success();
    }
}

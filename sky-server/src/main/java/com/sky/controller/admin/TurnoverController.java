package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.TurnoverService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("/admin/report")
@Api("数据报表接口")
public class TurnoverController {
    @Autowired
    private TurnoverService turnoverService;
    @ApiOperation("营业额统计")
    @GetMapping("/turnoverStatistics")
    public Result<TurnoverReportVO> getTurnoverReportStatics(@DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate begin
            ,@DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate end) {
        return Result.success(turnoverService.getTurnoverReport(begin,end));
    }

    @ApiOperation("查询销量前十")
    @GetMapping("/top10")
    public Result<SalesTop10ReportVO> getTop10(@DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate begin
            ,@DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate end){
        return Result.success(turnoverService.getTop10Stastics(begin,end));
    }

    @ApiOperation("用户数据查询")
    @GetMapping("/userStatistics")
    public Result<UserReportVO> getUserReport(@DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate begin
            ,@DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate end) {
        return Result.success(turnoverService.getUserReport(begin,end));
    }


    @ApiOperation("订单统计")
    @GetMapping("/ordersStatistics")
    public Result<OrderReportVO> getOrderReport(@DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate begin
            ,@DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate end){
        return Result.success(turnoverService.getOrderReport(begin,end));
    }
}

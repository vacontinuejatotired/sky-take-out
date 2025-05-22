package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.TurnoverService;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
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
}

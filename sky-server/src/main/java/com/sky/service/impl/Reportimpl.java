package com.sky.service.impl;
import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrdersMapper;
import com.sky.result.Result;
import com.sky.service.TurnoverService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class Reportimpl implements TurnoverService {
    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;

    public TurnoverReportVO getTurnoverReport(LocalDate begin, LocalDate end) {
        List<LocalDate> localDateList = new ArrayList<>();
        localDateList.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            localDateList.add(begin);
        }

        List<Double> turnover = new ArrayList<>();
        for (LocalDate localDate : localDateList) {
            Map map = new HashMap<>();
            LocalDateTime beginTime = LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);
            map.put("begin", beginTime);
            map.put("end", endTime);
            map.put("status", Orders.COMPLETED);
            Double turnovers = null;
            try {
                turnovers = ordersMapper.coutMoneyByMap(map);
            } catch (Exception e) {
                // 新增：打印异常堆栈信息
                System.err.println("执行SQL时发生异常: " + e.getMessage());
                e.printStackTrace();
                turnovers = 0.0; // 默认设为0，避免循环中断
            }
            turnovers = turnovers == null ? 0.0 : turnovers;
            System.out.println(turnovers);
            turnover.add(turnovers);
        }
        return TurnoverReportVO.builder()
                .turnoverList(StringUtils.join(turnover, ","))
                .dateList(StringUtils.join(localDateList, ","))
                .build();
    }

    @Override
    public SalesTop10ReportVO getTop10Stastics(LocalDate begin, LocalDate end) {
        List<LocalDate> localDateList = new ArrayList<>();
        localDateList.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            localDateList.add(begin);
        }
        List<GoodsSalesDTO> top10Dishes = orderDetailMapper.getTop10SellingDishes(
                LocalDateTime.of(begin, LocalTime.MIN),  // 开始时间
                LocalDateTime.of(end, LocalTime.MAX)    // 结束时间
        );

        // 3. 构建返回结果
        List<String> nameList = top10Dishes.stream()
                .map(GoodsSalesDTO::getName)
                .collect(Collectors.toList());

        List<Integer> numberList = top10Dishes.stream()
                .map(GoodsSalesDTO::getNumber)
                .collect(Collectors.toList());

        return SalesTop10ReportVO.builder()
                .nameList(StringUtils.join(nameList, ','))
                .numberList(StringUtils.join(numberList, ','))
                .build();
    }

    @Override
    public UserReportVO getUserReport(LocalDate begin, LocalDate end) {
        List<LocalDate> localDateList = new ArrayList<>();
        localDateList.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            localDateList.add(begin);
        }
        List<Integer>totalUserList = new ArrayList<>();
        List<Integer>newUserList = new ArrayList<>();
        for (LocalDate localDate : localDateList) {
            LocalDateTime beginTime = LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);
            Map map=new HashMap();
            map.put("end", beginTime);
            Integer total=ordersMapper.countUserByMap(map);
            map.put("end", endTime);
            map.put("begin", beginTime);
            Integer newTotal=ordersMapper.countUserByMap(map);
            totalUserList.add(total);
            newUserList.add(newTotal);
        }
        return UserReportVO.builder()
                .newUserList(StringUtils.join(newUserList,','))
                .dateList(StringUtils.join(localDateList,','))
                .totalUserList(StringUtils.join(totalUserList,','))
                .build();
    }

    @Override
    public OrderReportVO getOrderReport(LocalDate begin, LocalDate end) {
        List<LocalDate> localDateList = new ArrayList<>();
        localDateList.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            localDateList.add(begin);
        }
        List<Integer>totalOrderList = new ArrayList<>();
        List<Integer>validOrderList = new ArrayList<>();
        Integer totalOrderCount=0;
        Integer validOrderCount=0;

        for (LocalDate localDate : localDateList) {
//            LocalDateTime beginTime = LocalDateTime.of(localDate, LocalTime.MIN);
//            LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);
//            Integer total=getTotalOrderCount(beginTime,endTime,null);
//            totalOrderList.add(total);
//            Integer totalValid=getTotalOrderCount(beginTime,endTime,Orders.COMPLETED);
//            validOrderList.add(totalValid);
//            totalOrderCount+=total;
//            validOrderCount+=totalValid;
            LocalDateTime beginTime = LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);

            // 总订单数查询
            Integer total = 0;
            try {
                total = getTotalOrderCount(beginTime, endTime, null);
            } catch (Exception e) {
                log.error("查询日期 {} 的总订单数失败", localDate, e);
                // 失败时记录为 0 或其他默认值
            }
            totalOrderList.add(total);
            totalOrderCount += total;

            // 有效订单数查询
            Integer totalValid = 0;
            try {
                totalValid = getTotalOrderCount(beginTime, endTime, Orders.COMPLETED);
            } catch (Exception e) {
                log.error("查询日期 {} 的有效订单数失败", localDate, e);
                // 失败时记录为 0 或其他默认值
            }
            validOrderList.add(totalValid);
            validOrderCount += totalValid;
        }
        Double orderCompletionRate=(validOrderCount==0)?0.0:(double)validOrderCount/totalOrderCount;
        return OrderReportVO.builder()
                .validOrderCount(validOrderCount)
                .validOrderCountList(StringUtils.join(validOrderList,','))
                .orderCountList(StringUtils.join(totalOrderList,','))
                .orderCompletionRate(orderCompletionRate)
                .totalOrderCount(totalOrderCount)
                .dateList(StringUtils.join(localDateList,','))
                .build();
    }
    public Integer getTotalOrderCount(LocalDateTime begin, LocalDateTime end,Integer status) {
        Map map=new HashMap();
        map.put("begin", begin);
        map.put("end", end);
        map.put("status", status);
        return ordersMapper.getOrderCountByMap(map);
    }
}
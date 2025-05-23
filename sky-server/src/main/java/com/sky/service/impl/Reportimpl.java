package com.sky.service.impl;
import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrdersMapper;
import com.sky.result.Result;
import com.sky.service.TurnoverService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
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

    @Autowired
    private WorkspaceService workspaceService;

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
            turnover.add(turnovers);
        }
        return TurnoverReportVO.builder()
                .turnoverList(StringUtils.join(turnover, ","))
                .dateList(StringUtils.join(localDateList, ","))
                .build();
    }

    @Override
    public SalesTop10ReportVO getTop10Stastics(LocalDate begin, LocalDate end) {
        try {
            // 生成日期列表
            LocalDate temp = begin;
            List<LocalDate> localDateList = new ArrayList<>();
            localDateList.add(begin);
            while (!begin.equals(end)) {
                begin = begin.plusDays(1);
                localDateList.add(begin);
            }

            // 查询销量前10的菜品
            List<GoodsSalesDTO> top10Dishes = orderDetailMapper.getTop10SellingDishes(
                    LocalDateTime.of(temp, LocalTime.MIN),
                    LocalDateTime.of(end, LocalTime.MAX)
            );

            // 构建返回结果
            List<String> nameList = top10Dishes.stream()
                    .map(GoodsSalesDTO::getName)
                    .collect(Collectors.toList());

            List<Integer> numberList = top10Dishes.stream()
                    .map(GoodsSalesDTO::getNumber)
                    .collect(Collectors.toList());

            return SalesTop10ReportVO.builder()
                    .nameList(StringUtils.join(nameList, ","))
                    .numberList(StringUtils.join(numberList, ","))
                    .build();

        } catch (Exception e) {
            // 记录完整异常信息
            log.error("获取销量前10统计数据失败，开始日期：{}，结束日期：{}", begin, end, e);

            // 返回空结果或默认数据，避免前端异常
            return SalesTop10ReportVO.builder()
                    .nameList("")
                    .numberList("")
                    .build();
        }
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

    @Override
    public void exportBusinessData(HttpServletResponse response) {
        LocalDate begin = LocalDate.now().minusDays(30);
        LocalDate end=LocalDate.now().plusDays(1);
        BusinessDataVO businessDataVO = workspaceService.getBusinessData(LocalDateTime.of(begin,LocalTime.MIN),LocalDateTime.of(end,LocalTime.MAX));
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");
        try {
            // 基于模板文件创建一个新的Excel文件
            XSSFWorkbook excel = new XSSFWorkbook(resourceAsStream);

// 获取表格文件的Sheet页
            XSSFSheet sheet = excel.getSheet("Sheet1");

// 填充数据--时间
            sheet.getRow(1).getCell(1).setCellValue("时间：" + begin + "至" + end);

// 获得第4行
            XSSFRow row = sheet.getRow(3);
            row.getCell(2).setCellValue(businessDataVO.getTurnover());
            row.getCell(4).setCellValue(businessDataVO.getOrderCompletionRate());
            row.getCell(6).setCellValue(businessDataVO.getNewUsers());

// 获得第5行
            row = sheet.getRow(4);
            row.getCell(2).setCellValue(businessDataVO.getValidOrderCount());
            row.getCell(4).setCellValue(businessDataVO.getUnitPrice());
            // 填充明细数据
            for (int i = 0; i < 30; i++) {
                LocalDate date = begin.plusDays(i);
                // 查询某一天的营业数据
                BusinessDataVO businessData = workspaceService.getBusinessData(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));

                // 获得某一行
                row = sheet.getRow(7 + i);
                row.getCell(1).setCellValue(date.toString());
                row.getCell(2).setCellValue(businessData.getTurnover());
                row.getCell(3).setCellValue(businessData.getValidOrderCount());
                row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
                row.getCell(5).setCellValue(businessData.getUnitPrice());
                row.getCell(6).setCellValue(businessData.getNewUsers());
            }

// 3、通过输出流将Excel文件下载到客户端浏览器
            ServletOutputStream out = response.getOutputStream();
            excel.write(out);

// 关闭资源
            out.close();
            excel.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
    public Integer getTotalOrderCount(LocalDateTime begin, LocalDateTime end,Integer status) {
        Map map=new HashMap();
        map.put("begin", begin);
        map.put("end", end);
        map.put("status", status);
        return ordersMapper.getOrderCountByMap(map);
    }
}
package com.sky.service;

import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;

import java.time.LocalDate;

public interface TurnoverService {

    TurnoverReportVO getTurnoverReport(LocalDate begin, LocalDate end);

    SalesTop10ReportVO getTop10Stastics(LocalDate begin, LocalDate end);
}

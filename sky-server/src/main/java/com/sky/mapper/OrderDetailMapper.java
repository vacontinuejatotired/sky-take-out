package com.sky.mapper;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.OrderDetail;
import com.sky.vo.SalesTop10ReportVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderDetailMapper {
    public void insertBatch(List<OrderDetail> orderDetailList);

    List<OrderDetail> getByOrderid(Long orderId);

    @Delete("delete from sky_take_out.order_detail where order_id =#{id}")
    void deleteByOrderId(Long id);

    List<GoodsSalesDTO> getTop10SellingDishes(LocalDateTime of, LocalDateTime of1);
}

package com.sky.mapper;

import com.sky.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetMealDishMapper {
    /**
     * 这是插入口味表的方法
     * @param ids
     * @return
     */
    public List<Long> selectDishMealIdsByIds(List<Long> ids);


}

package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorsMapper {
    public void saveAll(List<DishFlavor> dishFlavors);

    @Delete("delete from dish_flavor where dish_id =#{dishId} ;")
    void deleteById(Long dishId);

    void deleteByIdConnections(List<Long> ids);

    List<DishFlavor> selectByDishId(Long dishId);

    List<DishFlavor> getByDishId(Long id);
}

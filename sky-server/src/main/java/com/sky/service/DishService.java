package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
    public void saveWithFal(DishDTO dishdto);

    PageResult pageQuery(DishPageQueryDTO dishDTO);

    void deleteAll(List<Long> ids);

    DishVO getById(Long id);

    void update(DishDTO dishDTO);

    List<Dish> listById(Long categoryId);

//    void changeStatus(Long id);
    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    List<DishVO> listWithFlavor(Dish dish);
}

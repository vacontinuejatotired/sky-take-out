package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;

public interface DishService {
    public void saveWithFal(DishDTO dishdto);

    PageResult pageQuery(DishPageQueryDTO dishDTO);
}

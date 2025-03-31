package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
    public void saveWithFal(DishDTO dishdto);

    PageResult pageQuery(DishPageQueryDTO dishDTO);

    void deleteAll(List<Long> ids);

    DishVO getById(Long id);

    void update(DishDTO dishDTO);

//    void changeStatus(Long id);
}

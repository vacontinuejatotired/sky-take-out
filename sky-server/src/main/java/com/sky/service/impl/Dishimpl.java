package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorsMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class Dishimpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorsMapper dishFlavorsMapper;
    @Override
    @Transactional
    public void saveWithFal(DishDTO dishdto) {
        Dish dish = new Dish();//DTO里面还有口味集合，不能直接放
        BeanUtils.copyProperties(dishdto, dish);
        dishMapper.saveWithFal(dish);
        Long id = dish.getId();
        List<DishFlavor> flavors=dishdto.getFlavors();
        if (flavors!=null&&flavors.size()>0) {
            flavors.forEach(flavor-> flavor.setDishId(id));
            dishFlavorsMapper.saveAll(flavors);
        }
    }

    @Override
    public PageResult pageQuery(DishPageQueryDTO dishDTO) {
        PageHelper.startPage(dishDTO.getPage(),dishDTO.getPageSize());
        Page<DishVO> page=dishMapper.pageQuery(dishDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }
}

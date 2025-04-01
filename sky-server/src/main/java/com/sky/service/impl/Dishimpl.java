package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorsMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class Dishimpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorsMapper dishFlavorsMapper;
    @Autowired
    private SetMealDishMapper setMealDishMapper;
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

    @
    Override
    @Transactional
    public void deleteAll(List<Long> ids) {
        //先检查状态
        for (Long id : ids) {
            Dish dish=dishMapper.getById(id);//拿到每个菜品的售卖状态
            if(Objects.equals(dish.getStatus(), StatusConstant.ENABLE)) {
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }
        //检查关联套餐
        List<Long> selectedDishMealIdsByIds = setMealDishMapper.selectDishMealIdsByIds(ids);
        if (selectedDishMealIdsByIds !=null&&!selectedDishMealIdsByIds.isEmpty()) {
            throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
        }
        //开始删除
//        for (Long id : ids) {
//            dishMapper.deleteById(id);
//            dishFlavorsMapper.deleteById(id);
//        }
        dishMapper.deleteByIdConnections(ids);
        dishFlavorsMapper.deleteByIdConnections(ids);
    }

    @Override
    public DishVO getById(Long id) {
        Dish dish= dishMapper.getById(id);
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish,dishVO);
        List<DishFlavor> flavors = dishFlavorsMapper.selectByDishId(id);
        dishVO.setFlavors(flavors);
        return dishVO;
    }

    @Override
    @Transactional
    public void update(DishDTO dishDTO) {
        Dish dish=new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.update(dish);
        dishFlavorsMapper.deleteById(dishDTO.getId());
        List<DishFlavor> flavors=dishDTO.getFlavors();
        if (flavors!=null&&flavors.size()>0) {
            flavors.forEach(flavor-> flavor.setDishId(dishDTO.getId()));
            dishFlavorsMapper.saveAll(flavors);
        }

    }

    @Override
    public List<Dish> listById(Long categoryId) {
        List<Dish> dish=dishMapper.getListDishByCategoryId(categoryId);
        return dish;
    }

//    @Override
//    public void changeStatus(Long id) {
//        // 1. 查询当前状态
//        Dish dish = dishMapper.getById(id);
//        if (dish == null) {
//            throw new RuntimeException("菜品不存在");
//        }
//
//        // 2. 判断当前状态并切换
//        Integer newStatus = dish.getStatus().equals(StatusConstant.ENABLE)
//                ? StatusConstant.DISABLE
//                : StatusConstant.ENABLE;
//
//        // 3. 通过 Mapper 更新数据库
//        dishMapper.setStatus(newStatus, id);
//    }
}

package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class Setmeallmpl implements SetMealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Override
    public SetmealVO getById(Long id) {
    SetmealVO vo = new SetmealVO();
        Setmeal setmeal=setmealMapper.getByCategoryId(id);
        if(setmeal==null){
            throw new SetmealEnableFailedException("套餐不存在");
        }
    List<SetmealDish> setmealDishes= setmealMapper.getSetmealDishsByCategoryId(id);
        if(setmealDishes==null){
            throw new SetmealEnableFailedException(MessageConstant.UNKNOWN_ERROR+"该套餐无菜品");
        }
        BeanUtils.copyProperties(setmeal,vo);
        vo.setSetmealDishes(setmealDishes);
        return vo;
    }
    /**
     *大概是先复制属性，然后开始插入到setmeal表
     * setmeal-dish表用动态sql插入，还是不用循环插入
     */
    @Override
    @Transactional
    public void saveSetmeal(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        try {
            setmealMapper.saveSetmeal(setmeal);
            log.info("插入后套餐ID: {}", setmeal.getId());
            // 2. 绑定套餐ID并保存菜品
            List<SetmealDish> dishes = setmealDTO.getSetmealDishes();
            if (dishes != null && !dishes.isEmpty()) {
                dishes.forEach(dish -> dish.setSetmealId(setmeal.getId())); // 关键：设置关联ID
                setmealMapper.saveSetmealDish(dishes); // 批量插入
            }
            log.info("事务提交成功");
        } catch (Exception e) {
            log.error("事务提交失败", e);
            throw e;
        }
    }

    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageDTO) {
        PageHelper.startPage(setmealPageDTO.getPage(),setmealPageDTO.getPageSize());
        Page<Setmeal> page=setmealMapper.pageQuerySetmeal(setmealPageDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void deleteById(Long ids) {
    Setmeal setmeal=setmealMapper.getBySetmealId(ids);
    if(setmeal==null){
        throw new SetmealEnableFailedException(MessageConstant.UNKNOWN_ERROR);
    }
        try {
            setmealMapper.deleteSetmeal(ids);
            setmealMapper.seleteSetmealDish(ids);
            log.info("删除套餐事务提交{}", setmeal.getId());
        } catch (Exception e) {
            log.info("fail to delete setmeal");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateSetmeal(SetmealDTO setmealDTO) {
        
    }
}

package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartimpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    @Override
    public void add(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.List(shoppingCart);
        if (shoppingCarts!=null&&shoppingCarts.size() > 0) {
            ShoppingCart shoppingCart1 = shoppingCarts.get(0);
            shoppingCart1.setNumber(shoppingCart1.getNumber()+1);
            shoppingCartMapper.updateNumberById(shoppingCart1);
        }
        else{
        Long id=shoppingCartDTO.getDishId();
        if(id!=null){
            Dish dish=dishMapper.getById(id);
            shoppingCart.setAmount(dish.getPrice());
            shoppingCart.setName(dish.getName());
            shoppingCart.setImage(dish.getImage());
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCart.setNumber(1);
        }
        else {
            Long setmealId=shoppingCartDTO.getSetmealId();
            Setmeal setmeal=setmealMapper.getBySetmealId(setmealId);
            shoppingCart.setAmount(setmeal.getPrice());
            shoppingCart.setName(setmeal.getName());
            shoppingCart.setImage(setmeal.getImage());
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCart.setNumber(1);
        }
        shoppingCartMapper.insertShoppingCart(shoppingCart);
        }
    }

    @Override
    public List<ShoppingCart> list(Long userid) {
        ShoppingCart shoppingCart =  new ShoppingCart();
        shoppingCart.setUserId(userid);
        return shoppingCartMapper.List(shoppingCart);
    }

    @Override
    public void clean(Long userid) {
        shoppingCartMapper.deleteByUserId(userid);
    }

    @Override
    public void deleteOne(ShoppingCartDTO shoppingCartDTO) {
    ShoppingCart shoppingCart = new ShoppingCart();
    BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
    shoppingCart=shoppingCartMapper.findById(shoppingCart);
    if(shoppingCart.getNumber()>0){
        shoppingCart.setNumber(shoppingCart.getNumber()-1);
        shoppingCartMapper.updateNumberById(shoppingCart);
    }
    else{
        shoppingCartMapper.deleteShoppingCart(shoppingCart);
    }
    }
}

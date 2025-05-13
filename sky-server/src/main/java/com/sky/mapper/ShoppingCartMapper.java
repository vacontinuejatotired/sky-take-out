package com.sky.mapper;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    List<ShoppingCart> List(ShoppingCart shoppingCart);

    @Update("update sky_take_out.shopping_cart set number =#{number} where id= #{id}")
    void updateNumberById(ShoppingCart shoppingCart1);

    @Insert("insert into sky_take_out.shopping_cart(name, image, user_id, dish_id, setmeal_id, dish_flavor, number, amount, create_time) VALUE " +
            "(#{name},#{image},#{userId},#{dishId},#{setmealId},#{dishFlavor},#{number},#{amount},#{createTime})")
    void insertShoppingCart(ShoppingCart shoppingCart);

    @Delete("delete from sky_take_out.shopping_cart where user_id =#{userid}")
    void deleteByUserId(Long userid);


    ShoppingCart findById(ShoppingCart shoppingCart);

    void deleteShoppingCart(ShoppingCart shoppingCart);

    void insertBatch(List<ShoppingCart> shoppingCartList);

}

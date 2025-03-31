package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);
    
    @AutoFill(value = OperationType.INSERT)
    void saveWithFal(Dish dish);

    Page<DishVO> pageQuery(DishPageQueryDTO dishDTO);

    @Select("select * from sky_take_out.dish where id = #{id}")
    Dish getById(Long id);

    @Delete("delete from dish where id =#{id};")
    void deleteById(Long id);

    void deleteByIdConnections(List<Long> ids);

    @AutoFill(value = OperationType.INSERT)
    void update(Dish dish);

//    @Update("update dish set status = #{disable} where id=#{id}")
//    void setStatus(@Param("disable") Integer disable, @Param("id") Long id);
}

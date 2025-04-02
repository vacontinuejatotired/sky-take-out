package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    /**
     * 根据套餐id查信息
     * 不是分类id
     * @param categoryId
     * @return
     */
    public List<SetmealDish> getSetmealDishsBySetmealId(Long categoryId);

    /**
     * 新增套餐
     * 只插入套餐表
     * @param setmeal
     */
    @AutoFill(OperationType.INSERT)
    void saveSetmeal(Setmeal setmeal);

    /**
     * 插入setmeal菜品表
     * @param setmealDishes
     */

    /**
     * 这个方法不需要更新时间
     * 不需要自动填充的注解
     * @param setmealDishes
     */
    void saveSetmealDish(List<SetmealDish> setmealDishes);

    /**
     * 分页查询
     * @param setmealPageDTO
     * @return
     */
    Page<Setmeal> pageQuerySetmeal(SetmealPageQueryDTO setmealPageDTO);

    /**
     * 从setmeal表删除指定id的setmeal
     * @param ids
     */
    @Delete("delete from setmeal where id=#{ids}")
    void deleteSetmeal(Long ids);

    /**
     * setmealdish表中删除带有指定套餐id的
     * @param ids
     */
    void deleteSetmealDish(Long ids);

    /**
     * 字面意思
     * @param ids
     * @return
     */
    @Select("select * from setmeal where id =#{ids}")
    Setmeal getBySetmealId(Long ids);

    @AutoFill(value = OperationType.UPDATE)
    int updateSetmeal(Setmeal setmeal);
}

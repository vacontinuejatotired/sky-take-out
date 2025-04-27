package com.sky.controller.admin;

import com.sky.annotation.AutoFill;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin/dish")
@Slf4j
@Api(tags = "菜品接口")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;
    @ApiOperation(value = "新增菜品")
    @PostMapping
    public Result save(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品{}", dishDTO);
        dishService.saveWithFal(dishDTO);
        String key="dish_"+dishDTO.getId();
        redisTemplate.delete(key);
        return Result.success();
    }
    @ApiOperation("分页查询")
    @GetMapping("/page")
    public Result<PageResult> page(DishPageQueryDTO dishDTO) {
        log.info("分页查询{}", dishDTO);
        PageResult pageResult=dishService.pageQuery(dishDTO);
        return Result.success(pageResult);
    }

    @ApiOperation("批量删除菜品")
    @DeleteMapping()
    public Result delete(@RequestParam List<Long> ids) {
        log.info("delete:{}", ids);
        dishService.deleteAll(ids);
        Set keys=redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查菜品")
    public Result<DishVO> get(@PathVariable Long id) {
        log.info("get:{}", id);
        DishVO dishVO= dishService.getById(id);
        return Result.success(dishVO);
    }

    @ApiOperation("起售停售菜品")
    @PostMapping("/status/{status}")
    public Result update(@PathVariable Integer status,@RequestParam Long id) {
        log.info("update status:{}", id);
        DishDTO dishDTO = new DishDTO();
        dishDTO.setId(id);
        dishDTO.setStatus(status);
        dishService.update(dishDTO);
        Set<String> keys=redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);
        return Result.success();
    }

    @PutMapping()
    @ApiOperation("修改菜品")
    public Result update(@RequestBody DishDTO dishDTO) {
        log.info("update:{}", dishDTO);
        dishService.update(dishDTO);
        Set<String> keys=redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);
        return Result.success();
    }

    @ApiOperation("根据分类id查菜品")
    @GetMapping("/list")
    public Result<List<Dish>> list(Long categoryId) {
        log.info("list:{}", categoryId);
        List<Dish>dish= dishService.listById(categoryId);
        return Result.success(dish);
    }
//    @ApiOperation("修改菜品售卖状态")
//    @PostMapping( "/status/{status}")
//    public Result changeStatus(@PathVariable("status") Long status){
//        log.info("Meal changeStatus:{}", status);
    //没有传id给我，改不了
//        dishService.changeStatus(id);
//        return Result.success();
//    }
}

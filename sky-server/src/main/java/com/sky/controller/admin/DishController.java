package com.sky.controller.admin;

import com.sky.annotation.AutoFill;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dish")
@Slf4j
@Api(tags = "菜品接口")
public class DishController {
    @Autowired
    private DishService dishService;
    @ApiOperation(value = "新增菜品")
    @PostMapping
    public Result save(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品{}", dishDTO);
        dishService.saveWithFal(dishDTO);
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
        return Result.success();
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

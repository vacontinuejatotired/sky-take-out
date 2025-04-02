package com.sky.controller.admin;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "套餐接口")
@Slf4j
@RestController
@RequestMapping("/admin/setmeal")
public class SetMealController {
    @Autowired
    private SetMealService setMealService;

    @GetMapping("/{id}")
    @ApiOperation("查找套餐")
    public Result<SetmealVO> getById(@PathVariable Long id) {
        //传进来的是setmeal表的主键id
        //需要注意之后查询的时候使用id情景
        log.info("getById {}",id);
        SetmealVO setmealVO= setMealService.getById(id);
        return Result.success(setmealVO);
    }

    @PostMapping
    @ApiOperation("新增套餐")
    public Result save(@RequestBody SetmealDTO setmealDTO) {
        log.info("save {}",setmealDTO);
        setMealService.saveSetmeal(setmealDTO);
        return Result.success();
    }

    @ApiOperation("分页查询套餐")
    @GetMapping("/page")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("page {}",setmealPageQueryDTO);
        PageResult pageResult=setMealService.pageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * nmd,前端给你指定方法，就不要在路径里写delete路径，除非多个删除方法
     * cnmd
     * @param ids
     * @return
     */
    @ApiOperation("删除套餐")
    @DeleteMapping
    public Result delete(Long ids) {
        log.info("delete {}",ids);
        setMealService.deleteById(ids);
        return Result.success();
    }

    @ApiOperation("修改套餐")
    @PutMapping
    public Result update(@RequestBody SetmealDTO setmealDTO) {
        log.info("update {}",setmealDTO);
        setMealService.updateSetmeal(setmealDTO);
        return Result.success();
    }

}
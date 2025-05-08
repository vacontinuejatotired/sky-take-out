package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "购物车接口")
@RequestMapping("/user/shoppingCart")
@Slf4j
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @ApiOperation("添加")
    @PostMapping("/add")
    public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("add shoppingCartDTO:{}",shoppingCartDTO);
        shoppingCartService.add(shoppingCartDTO);
        return Result.success();
    }

    @ApiOperation("购物车查询")
    @GetMapping("/list")
    public Result<List<ShoppingCart>> list() {
        log.info("list shoppingCart");
        Long userid= BaseContext.getCurrentId();
        List<ShoppingCart>list=shoppingCartService.list(userid);
        return Result.success(list);
    }

    @ApiOperation("根据用户id清空购物车")
    @DeleteMapping("/clean")
    public Result clean() {
        log.info("clean shoppingCart");
        Long userid= BaseContext.getCurrentId();
        shoppingCartService.clean(userid);
        return Result.success();
    }
    @ApiOperation("特定删除菜品")
    @PostMapping("/sub")
    public Result deleteone(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("sub shoppingCartDTO:{}",shoppingCartDTO);
        shoppingCartService.deleteOne(shoppingCartDTO);
        return Result.success();
    }
}

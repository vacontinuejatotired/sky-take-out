package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }
    @ApiOperation("增加员工")
    @PostMapping
    public Result save(@RequestBody EmployeeDTO employee) {
        Employee employee1 = new Employee();
        log.info("新增员工{}", employee);
        employeeService.save(employee);
        return Result.success();
    }
    @GetMapping("/page")
    @ApiOperation("分页查询")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("分页查询{}", employeePageQueryDTO);
        PageResult pageResult=employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 启用员工或者禁用员工账号
     */
    @ApiOperation("修改员工账号使用情况")
    @PostMapping("/status/{status}")
    public Result updateStatus(@PathVariable("status") Integer status,long id) {
        log.info("正在修改:status= {},id= {}", status,id);
        employeeService.update(status,id);
        return Result.success();
    }

    /**
     * 根据id查询
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询")
    public Result<Employee> getEmployeeById(@PathVariable("id") Long id) {
        log.info("根据:id={}查询员工 ", id);
        Employee employee=employeeService.getById(id);
        return Result.success(employee);
    }

    @PutMapping
    @ApiOperation("修改员工")
    public Result update(@RequestBody EmployeeDTO employeeDTO) {
        log.info("修改员工{}", employeeDTO);
        employeeService.update(employeeDTO);
        return Result.success();
    }
}
package com.back.controller.user;

import com.back.constant.MessageConstant;
import com.back.dto.OrderDTO;
import com.back.entity.Order;
import com.back.result.Result;
import com.back.service.OrderService;
import com.back.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/user/order")
@Api(tags = "用户端订单相关接口")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/{id}")
    @ApiOperation("查询订单信息")
    public Result<List<Order>> getOrderById(@PathVariable("id") Long userId, Integer status) {

        List<Order> order = orderService.getOrderInfo(userId);
        return Result.success(order);
    }

    @PostMapping
    @ApiOperation("创建新订单信息")
    public Result getOrderById(@RequestBody OrderDTO orderDTO) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String deliveryTime = orderDTO.getDeliveryTime();
        try {
            LocalDateTime.parse(deliveryTime, formatter);
        } catch (DateTimeParseException e) {
            return Result.error(MessageConstant.DATETIME_ERROR);
        }
        orderService.createOrderInfo(orderDTO);
        return Result.success();
    }

    @PutMapping
    @ApiOperation("更新订单信息")
    public Result updateOrderById(@RequestBody OrderDTO orderDTO) {
        orderService.updateOrderInfo(orderDTO);

        return Result.success();
    }
}

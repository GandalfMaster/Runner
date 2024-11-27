package com.back.controller.admin;

import com.back.entity.Order;
import com.back.result.Result;
import com.back.service.OrderService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin/order")
@Api(tags = "管理员端订单相关接口")
public class OrderAdminController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public Result<List<Order>> selectAllOrder(){
        List<Order> orders = orderService.getOrder();
        return Result.success(orders);
    }

    @GetMapping("/{id}")
    public Result<List<Order>> selectOrderById(@PathVariable Long id, Integer status){
        List<Order> orders = orderService.getOrderInfoFromStatus(id,status);
        return Result.success(orders);
    }
}

package com.back.controller.user;

import com.back.dto.RiderDTO;
import com.back.entity.Order;
import com.back.result.Result;
import com.back.service.RiderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/rider")
@Api(tags = "用户端骑手相关接口")
@Slf4j
public class RiderController {

    @Autowired
    private RiderService riderService;

    @GetMapping("/order")
    @ApiOperation("查询订单信息")
    public Result<List<Order>> getOrderById() {

        List<Order> order = riderService.getOrderInfoForRider();
        return Result.success(order);
    }

    @PutMapping("/accept")
    @ApiOperation("骑手接单")
    public Result acceptOrder(@RequestBody RiderDTO riderDTO) {

        riderService.updateOrderInfo(riderDTO);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据骑手ID查询订单信息")
    public Result<List<Order>> getOrderById(@PathVariable("id") Long riderId, Integer status) {

        List<Order> order = riderService.getOrderInfo(riderId);
        return Result.success(order);
    }
}

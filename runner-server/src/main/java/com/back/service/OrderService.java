package com.back.service;

import com.back.dto.OrderDTO;
import com.back.entity.Order;

import java.util.List;

public interface OrderService {

    List<Order> getOrderInfo(Long id);

    void createOrderInfo(OrderDTO orderDTO);
}

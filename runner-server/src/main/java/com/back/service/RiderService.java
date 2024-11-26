package com.back.service;

import com.back.dto.RiderDTO;
import com.back.entity.Order;

import java.util.List;

public interface RiderService {

    List<Order> getOrderInfoForRider();

    List<Order> getOrderInfo(Long id);

    void updateOrderInfo(RiderDTO riderDTO);
}

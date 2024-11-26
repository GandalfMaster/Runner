package com.back.service.impl;

import com.back.constant.MessageConstant;
import com.back.dto.RiderDTO;
import com.back.entity.Order;
import com.back.exception.OrderBusinessException;
import com.back.mapper.OrderMapper;
import com.back.mapper.RiderMapper;
import com.back.service.RiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RiderServiceImpl implements RiderService {

    @Autowired
    private RiderMapper riderMapper;



    public List<Order> getOrderInfoForRider() {
        List<Order> orderByStatus = riderMapper.getByStatus();
        return orderByStatus;

    }

    public void updateOrderInfo(RiderDTO riderDTO) {
        riderMapper.update(riderDTO);
    }

    public List<Order> getOrderInfo(Long riderid){
        List<Order> order = riderMapper.getByRiderid(riderid);
        if(order == null){
            throw new OrderBusinessException(MessageConstant.TICKET_FAILED);
        }
        return order;
    }
}

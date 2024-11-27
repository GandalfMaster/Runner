package com.back.service.impl;

import com.back.constant.MessageConstant;
import com.back.dto.OrderDTO;
import com.back.entity.Order;
import com.back.exception.OrderBusinessException;
import com.back.mapper.OrderMapper;
import com.back.service.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 根据用户 ID 查询订单信息
     *
     * @param userid 用户 ID
     * @return 返回订单信息或错误
     */
    public List<Order> getOrderInfo(Long userid){
        List<Order> order = orderMapper.getByUserid(userid);
        if(order == null){
            throw new OrderBusinessException(MessageConstant.TICKET_FAILED);
        }
        return order;
    }


    public void createOrderInfo(OrderDTO orderDTO) {
        Order order = new Order();
        BeanUtils.copyProperties(orderDTO, order);

        orderMapper.insert(order);
    }

    public void updateOrderInfo(OrderDTO orderDTO){
        Order order = new Order();
        BeanUtils.copyProperties(orderDTO, order);

        orderMapper.update(order);
    }

    public List<Order> getOrder(){
        return orderMapper.getOrder();
    }


    public List<Order> getOrderInfoFromStatus(Long id, Integer status) {

        List<Order> order = orderMapper.getByStatusAndId(id, status);
        if(order == null){
            throw new OrderBusinessException(MessageConstant.STATUS_FAILED);
        }
        return order;
    }


}

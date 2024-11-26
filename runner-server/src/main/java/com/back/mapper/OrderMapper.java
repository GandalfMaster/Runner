package com.back.mapper;

import com.back.annotation.AutoFill;
import com.back.entity.Order;
import com.back.entity.User;
import com.back.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderMapper {


    @Select("select * from `orders` where user_id = #{userid}")
    List<Order> getByUserid(Long user_id);

    //@AutoFill(value = OperationType.INSERT)
    void insert(Order order);

    @Select("select * from `orders`")
    List<Order> getOrder();

}

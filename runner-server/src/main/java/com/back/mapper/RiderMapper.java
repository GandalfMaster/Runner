package com.back.mapper;

import com.back.dto.RiderDTO;
import com.back.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface RiderMapper {

    @Select("select * from orders where status = 0")
    List<Order> getByStatus();

    @Select("select * from orders where rider_id = #{riderId}")
    List<Order> getByRiderid(Long riderId);

    @Update("Update orders set rider_id = #{riderId} WHERE order_id = #{orderId}")
    void update(RiderDTO riderDTO);
}

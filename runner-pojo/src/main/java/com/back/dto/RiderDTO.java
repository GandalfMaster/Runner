package com.back.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class RiderDTO implements Serializable {

    private Long orderId;
    private Long riderId;

}

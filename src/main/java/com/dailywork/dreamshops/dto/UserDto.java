package com.dailywork.dreamshops.dto;

import com.dailywork.dreamshops.model.Cart;
import lombok.Data;

import java.util.List;
@Data
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private List<OrderDto> orders;
    private CartDto cart;

}

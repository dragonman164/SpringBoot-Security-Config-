package com.dailywork.dreamshops.service.cart;

import com.dailywork.dreamshops.model.Cart;
import com.dailywork.dreamshops.model.User;

import java.math.BigDecimal;

public interface ICartService {

    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);
    Cart getCartByUserId(Long userId);


    Cart initializeNewCart(User user);
}

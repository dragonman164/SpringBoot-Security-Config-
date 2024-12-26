package com.dailywork.dreamshops.repository;

import com.dailywork.dreamshops.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long > {

    List<Order> findByUserId(Long userId);
}

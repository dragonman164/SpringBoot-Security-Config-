package com.dailywork.dreamshops.repository;

import com.dailywork.dreamshops.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryName(String category);

    List<Product> findByBrand(String brand);

    List<Product> findByCategoryNameAndBrand(String category, String brand);

    List<Product> findByName(String name);

    List<Product> findByBrandAndName(String brand, String name);

    Long countProductsByBrandAndName(String brand, String name);

    boolean existsByNameAndBrand(String name, String brand);
}

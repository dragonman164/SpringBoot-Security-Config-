package com.dailywork.dreamshops.controller;

import com.dailywork.dreamshops.dto.ProductDto;
import com.dailywork.dreamshops.exceptions.AlreadyExistsException;
import com.dailywork.dreamshops.exceptions.ResourceNotFoundException;
import com.dailywork.dreamshops.model.Product;
import com.dailywork.dreamshops.request.AddProductRequest;
import com.dailywork.dreamshops.request.ProductUpdateRequest;
import com.dailywork.dreamshops.response.ApiResponse;
import com.dailywork.dreamshops.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {

    private final IProductService productService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
        return ResponseEntity.ok(
                new ApiResponse("All products fetched successfully!", convertedProducts)
        );
    }

    @GetMapping("product/{productId}/product")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {
        try {
            Product product = productService.getProductById(productId);
            ProductDto convertedProduct = productService.convertToDto(product);
            return ResponseEntity.ok(
                    new ApiResponse("Product fetched successfully!", convertedProduct)
            );
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Product not found!", null));
        }

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest request) {
            try {
                Product newProduct = productService.addProduct(request);
                ProductDto convertedProduct = productService.convertToDto(newProduct);

                return ResponseEntity.ok(
                        new ApiResponse("Product added successfully!", convertedProduct)
                );
            } catch (AlreadyExistsException e) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ApiResponse("Product already exists!", null));
            }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/product/{productId}/update")
    public ResponseEntity<ApiResponse> productUpdate(@RequestBody ProductUpdateRequest request, @PathVariable Long productId) {
        try {
            Product updatedProduct = productService.updateProduct(request, productId);
            ProductDto convertedProduct = productService.convertToDto(updatedProduct);

            return ResponseEntity.ok(
                    new ApiResponse("Product updated successfully!", convertedProduct)
            );
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Product not found!", null));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/product/{productId}/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) {
        try {
            productService.deleteProductById(productId);
            return ResponseEntity.ok(
                    new ApiResponse("Product deleted successfully!", null)
            );
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Product not found!", null));
        }
    }

    @GetMapping("/by/brand-and-name")
    public ResponseEntity<ApiResponse> getProductByBrandAndName(@RequestParam String brand, @RequestParam String name) {
        try {
            List<Product> products = productService.getProductsByBrandAndName(brand, name);
            List<ProductDto> convertedProducts = productService.getConvertedProducts(products);

            if(products.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("Product not found!", null));
            }
            return ResponseEntity.ok(
                    new ApiResponse("Products fetched successfully!", convertedProducts)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Could not fetch products!", e.getMessage()));
        }
    }

    @GetMapping("/by/category-and-brand")
    public ResponseEntity<ApiResponse> getProductByCategoryAndBrand(@RequestParam String category, @RequestParam String brand) {
        try {
            List<Product> products = productService.getProductsByCategoryAndBrand(category, brand);
            List<ProductDto> convertedProducts = productService.getConvertedProducts(products);

            if(products.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("Product not found!", null));
            }
            return ResponseEntity.ok(
                    new ApiResponse("Products fetched successfully!", convertedProducts)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Could not fetch products!", e.getMessage()));
        }
    }

    @GetMapping("/products/{name}/products")
    public ResponseEntity<ApiResponse> getProductByName(@PathVariable String name) {
        try {
            List<Product> products = productService.getProductsByName(name);
            List<ProductDto> convertedProducts = productService.getConvertedProducts(products);


            if(products.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("Product not found!", null));
            }
            return ResponseEntity.ok(
                    new ApiResponse("Products fetched successfully!", convertedProducts)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Could not fetch products!", e.getMessage()));
        }
    }

    @GetMapping("/product/by-brand")
    public ResponseEntity<ApiResponse> findProductByBrand(@RequestParam String brand) {
        try {
            List<Product> products = productService.getProductsByBrand(brand);
            List<ProductDto> convertedProducts = productService.getConvertedProducts(products);

            if (products.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("No products found ", null));
            }

            return ResponseEntity.ok(
                    new ApiResponse("Products fetched successfully!", convertedProducts)
            );
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/product/{category}/all/products")
    public ResponseEntity<ApiResponse> findProductByCategory(@PathVariable String category) {
        try {
            List<Product> products = productService.getProductsByCategory(category);
            List<ProductDto> convertedProducts = productService.getConvertedProducts(products);

            if (products.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("No products found ", null));
            }

            return ResponseEntity.ok(
                    new ApiResponse("Products fetched successfully!", convertedProducts)
            );
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/product/count/by-brand/and-name")
    public ResponseEntity<ApiResponse> countProductsByBrandAndName(@RequestParam String brand, @RequestParam String name) {
        try {
            Long count = productService.countProductsByBrandAndName(brand, name);
            return ResponseEntity.ok(
                    new ApiResponse("Products count fetched successfully!", count)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Could not fetch products count!", e.getMessage()));
        }
    }



}

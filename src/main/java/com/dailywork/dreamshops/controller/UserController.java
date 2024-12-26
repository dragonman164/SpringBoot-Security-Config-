package com.dailywork.dreamshops.controller;

import com.dailywork.dreamshops.dto.UserDto;
import com.dailywork.dreamshops.exceptions.AlreadyExistsException;
import com.dailywork.dreamshops.exceptions.ResourceNotFoundException;
import com.dailywork.dreamshops.model.User;
import com.dailywork.dreamshops.request.CreateUserRequest;
import com.dailywork.dreamshops.request.UserUpdateRequest;
import com.dailywork.dreamshops.response.ApiResponse;
import com.dailywork.dreamshops.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {
    private final IUserService userService;

    @GetMapping("/{userId}/user")
    public ResponseEntity<ApiResponse> getUserById(
            @PathVariable Long userId
    ){
        try {
            User user = userService.getUserById(userId);
            UserDto userDto = userService.convertUserToDto(user);

            return ResponseEntity.ok(new ApiResponse("User found!", userDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("User not found!", e.getMessage()));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createUser(
            @RequestBody CreateUserRequest createUserRequest

    ){
        try {
            User user = userService.createUser(createUserRequest);
            UserDto userDto = userService.convertUserToDto(user);
            return ResponseEntity.ok(new ApiResponse("User created successfully!", userDto));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse("User already exists!", e.getMessage()));
        }
    }

    @PutMapping("/{userId}/update")
    public ResponseEntity<ApiResponse> updateUser(
            @PathVariable Long userId,
            @RequestBody UserUpdateRequest updateRequest
            )
    {

        try {
            User user = userService.updateUser(updateRequest,userId);
            UserDto userDto = userService.convertUserToDto(user);

            return ResponseEntity.ok(new ApiResponse("User updated successfully!", userDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("User not found!", e.getMessage()));
        }

    }

    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<ApiResponse> deleteUser(
            @PathVariable Long userId
    ){
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok(new ApiResponse("User deleted successfully!", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("User not found!", e.getMessage()));
        }
    }







}

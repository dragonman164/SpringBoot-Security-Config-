package com.dailywork.dreamshops.service.user;

import com.dailywork.dreamshops.dto.UserDto;
import com.dailywork.dreamshops.model.User;
import com.dailywork.dreamshops.request.CreateUserRequest;
import com.dailywork.dreamshops.request.UserUpdateRequest;

public interface IUserService {
    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(UserUpdateRequest request, Long userId);
    void deleteUser(Long userId);

    UserDto convertUserToDto(User user);

    User getAuthenticatedUser();
}

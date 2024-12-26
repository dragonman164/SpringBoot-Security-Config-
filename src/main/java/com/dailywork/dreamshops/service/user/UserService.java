package com.dailywork.dreamshops.service.user;


import com.dailywork.dreamshops.dto.UserDto;
import com.dailywork.dreamshops.exceptions.AlreadyExistsException;
import com.dailywork.dreamshops.exceptions.ResourceNotFoundException;
import com.dailywork.dreamshops.model.User;
import com.dailywork.dreamshops.repository.UserRepository;
import com.dailywork.dreamshops.request.CreateUserRequest;
import com.dailywork.dreamshops.request.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;


    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(()->
                        new ResourceNotFoundException("User not found with id: "+userId));
    }

    @Override
    public User createUser(CreateUserRequest request) {
        return Optional.of(request)
                .filter(user->!userRepository.existsByEmail(user.getEmail()))
                .map(user->{
                    User newUser = new User();
                    newUser.setFirstName(user.getFirstName());
                    newUser.setLastName(user.getLastName());
                    newUser.setEmail(user.getEmail());
                    newUser.setPassword(passwordEncoder.encode(user.getPassword()));
                    return userRepository.save(newUser);
                }).orElseThrow(()->new AlreadyExistsException("User already exists with email: "+request.getEmail()));
    }

    @Override
    public User updateUser(UserUpdateRequest request, Long userId) {
        return userRepository.findById(userId).map(existingUser->
        {
            existingUser.setFirstName(request.getFirstName());
            existingUser.setLastName(request.getLastName());
            return userRepository.save(existingUser);
        }).orElseThrow(
                ()->new ResourceNotFoundException("User not found with id: "+userId)
        );
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId)
                .ifPresentOrElse(userRepository::delete
                ,
                        ()->{
                    throw new ResourceNotFoundException("User not found with id: "+userId);
                });
    }

    @Override
    public UserDto convertUserToDto(User user){
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email);
    }

}

package com.example.bookshop.service.impl;

import com.example.bookshop.dto.user.UserRegistrationRequestDto;
import com.example.bookshop.dto.user.UserResponseDto;
import com.example.bookshop.exception.RegistrationException;
import com.example.bookshop.mapper.UserMapper;
import com.example.bookshop.model.User;
import com.example.bookshop.repository.UserRepository;
import com.example.bookshop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (isExist(requestDto.getEmail())) {
            throw new RegistrationException(String.format(
                    "User with email %s already exists!",
                    requestDto.getEmail()
            ));
        }
        User user = userMapper.toModel(requestDto);
        return userMapper.toDto(userRepository.save(user));
    }

    public boolean isExist(String email) {
        return userRepository.existsByEmail(email);
    }
}

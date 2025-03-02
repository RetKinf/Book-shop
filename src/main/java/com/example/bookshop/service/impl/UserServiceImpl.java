package com.example.bookshop.service.impl;

import com.example.bookshop.dto.user.UserRegistrationRequestDto;
import com.example.bookshop.dto.user.UserResponseDto;
import com.example.bookshop.exception.EntityNotFoundException;
import com.example.bookshop.exception.RegistrationException;
import com.example.bookshop.mapper.UserMapper;
import com.example.bookshop.model.Role;
import com.example.bookshop.model.RoleName;
import com.example.bookshop.model.User;
import com.example.bookshop.repository.RoleRepository;
import com.example.bookshop.repository.UserRepository;
import com.example.bookshop.service.CartService;
import com.example.bookshop.service.UserService;
import jakarta.transaction.Transactional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartService cartService;

    @Transactional
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
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Role %s Not Found", RoleName.USER)
                ));
        user.setRoles(Set.of(role));
        User saveUser = userRepository.save(user);
        cartService.createShoppingCart(saveUser);
        return userMapper.toDto(saveUser);
    }

    public boolean isExist(String email) {
        return userRepository.existsByEmail(email);
    }
}

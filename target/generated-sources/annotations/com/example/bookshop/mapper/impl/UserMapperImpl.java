package com.example.bookshop.mapper.impl;

import com.example.bookshop.dto.user.UserRegistrationRequestDto;
import com.example.bookshop.dto.user.UserResponseDto;
import com.example.bookshop.mapper.UserMapper;
import com.example.bookshop.model.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-12T12:11:32+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toModel(UserRegistrationRequestDto userRegistrationRequestDto) {
        if ( userRegistrationRequestDto == null ) {
            return null;
        }

        User user = new User();

        if ( userRegistrationRequestDto.getEmail() != null ) {
            user.setEmail( userRegistrationRequestDto.getEmail() );
        }
        if ( userRegistrationRequestDto.getPassword() != null ) {
            user.setPassword( userRegistrationRequestDto.getPassword() );
        }
        if ( userRegistrationRequestDto.getFirstName() != null ) {
            user.setFirstName( userRegistrationRequestDto.getFirstName() );
        }
        if ( userRegistrationRequestDto.getLastName() != null ) {
            user.setLastName( userRegistrationRequestDto.getLastName() );
        }
        if ( userRegistrationRequestDto.getShippingAddress() != null ) {
            user.setShippingAddress( userRegistrationRequestDto.getShippingAddress() );
        }

        return user;
    }

    @Override
    public UserResponseDto toDto(User user) {
        if ( user == null ) {
            return null;
        }

        Long id = null;
        String email = null;
        String firstName = null;
        String lastName = null;
        String shippingAddress = null;

        if ( user.getId() != null ) {
            id = user.getId();
        }
        if ( user.getEmail() != null ) {
            email = user.getEmail();
        }
        if ( user.getFirstName() != null ) {
            firstName = user.getFirstName();
        }
        if ( user.getLastName() != null ) {
            lastName = user.getLastName();
        }
        if ( user.getShippingAddress() != null ) {
            shippingAddress = user.getShippingAddress();
        }

        UserResponseDto userResponseDto = new UserResponseDto( id, email, firstName, lastName, shippingAddress );

        return userResponseDto;
    }
}

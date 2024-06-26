package com.kingmartinien.iutnotifyapi.mapper;

import com.kingmartinien.iutnotifyapi.dto.CreateUserDto;
import com.kingmartinien.iutnotifyapi.dto.UserDto;
import com.kingmartinien.iutnotifyapi.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    User toEntity(CreateUserDto createUserDto);

    @Mapping(source = "fullName", target = "fullName")
    UserDto toDto(User user);

    List<UserDto> toDto(List<User> users);

}

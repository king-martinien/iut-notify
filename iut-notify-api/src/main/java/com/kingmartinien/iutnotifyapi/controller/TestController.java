package com.kingmartinien.iutnotifyapi.controller;

import com.kingmartinien.iutnotifyapi.dto.UserDto;
import com.kingmartinien.iutnotifyapi.mapper.UserMapper;
import com.kingmartinien.iutnotifyapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("test")
@RequiredArgsConstructor
public class TestController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String test() {
        return "test work!";
    }

    @GetMapping("users")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERMISSION_CREATE_CHANNEL')" )
    public List<UserDto> getAllUsers() {
        return this.userMapper.toDto(this.userService.getAllUsers());
    }

}

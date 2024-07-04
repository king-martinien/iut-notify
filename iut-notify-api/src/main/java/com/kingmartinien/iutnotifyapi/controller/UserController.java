package com.kingmartinien.iutnotifyapi.controller;

import com.kingmartinien.iutnotifyapi.dto.UploadResult;
import com.kingmartinien.iutnotifyapi.enums.RoleEnum;
import com.kingmartinien.iutnotifyapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.OK)
    public UploadResult uploadUsers(@RequestParam("file") MultipartFile file, @RequestParam("role") RoleEnum role) throws IOException {
        return this.userService.uploadUsers(file, role);
    }

}

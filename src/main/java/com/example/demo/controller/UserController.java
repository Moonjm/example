package com.example.demo.controller;

import com.example.demo.dto.UserDto.InsertUserRequest;
import com.example.demo.model.BasicResponse;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class UserController {

    private final UserService userService;

    @GetMapping("v1/users/{userId}")
    public ResponseEntity<? extends BasicResponse> getUsers(
            @PathVariable("userId") String userId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserDetail(userId));
    }

    @PostMapping("v1/users")
    public ResponseEntity<? extends BasicResponse> insertUser(
            @RequestBody InsertUserRequest dto,
            WebRequest webRequest
    ) {
        webRequest.setAttribute("body", dto, RequestAttributes.SCOPE_REQUEST);
        return ResponseEntity.status(HttpStatus.OK).body(userService.insertUser(dto));
    }

    @PostMapping("v1/users/{userId}/image")
    public ResponseEntity<? extends BasicResponse> insertUserImage(
            @PathVariable("userId") String userId,
            @RequestParam(name = "image") MultipartFile image
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.insertUserImage(userId, image));
    }

    @GetMapping("v1/users/{userId}/image")
    public ResponseEntity<byte[]> getUserImage(
            @PathVariable("userId") String userId
    ) {
        return userService.getUserImage(userId);
    }

    @DeleteMapping("v1/users/{userId}")
    public ResponseEntity<? extends BasicResponse> deleteUser(
            @PathVariable("userId") String userId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.deleteUser(userId));
    }
}

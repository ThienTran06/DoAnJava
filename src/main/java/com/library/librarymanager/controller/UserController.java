package com.library.librarymanager.controller;

import com.library.librarymanager.dto.request.CreateUserRequest;
import com.library.librarymanager.entity.NguoiDung;
import com.library.librarymanager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class UserController {
    @Autowired
    private UserService sv;

    @PostMapping("/Create")
    public NguoiDung create(@Valid @RequestBody CreateUserRequest req) {

        return sv.create(req);
    }}

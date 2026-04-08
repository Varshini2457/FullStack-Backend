package com.klef.fsad.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.klef.fsad.dto.LoginRequest;
import com.klef.fsad.dto.LoginResponse;
import com.klef.fsad.service.UserService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/")
public class AuthController {
	@Autowired
	private UserService userService;

	@PostMapping("/login")
	public LoginResponse login(@RequestBody LoginRequest request) {
		System.out.println("EMAIL: " + request.getEmail());
		System.out.println("PASSWORD: " + request.getPassword());
		System.out.println("ROLE: " + request.getRole());

		String token = userService.login(request.getEmail(), request.getPassword(), request.getRole());
		return new LoginResponse(token);
	}

}

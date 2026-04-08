package com.klef.fsad.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.klef.fsad.dto.RegisterRequest;
import com.klef.fsad.model.User;
import com.klef.fsad.service.UserService;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/")
public class UserController 
{
	@Autowired
	private UserService userService;
	@PostMapping("/register")
	public User register(@RequestBody RegisterRequest request)
	{
		return userService.register(request);
	}
	

}

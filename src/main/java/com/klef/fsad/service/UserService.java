package com.klef.fsad.service;

import com.klef.fsad.dto.RegisterRequest;
import com.klef.fsad.model.User;

public interface UserService 
{
	User register(RegisterRequest request);
	String login(String email, String password,String role);

}

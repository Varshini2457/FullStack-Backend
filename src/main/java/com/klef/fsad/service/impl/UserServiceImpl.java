package com.klef.fsad.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.klef.fsad.dto.RegisterRequest;
import com.klef.fsad.model.User;
import com.klef.fsad.repository.UserRepository;
import com.klef.fsad.security.JwtUtil;
import com.klef.fsad.service.EmailService;
import com.klef.fsad.service.UserService;

import jakarta.validation.constraints.Null;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtUtil jwtutil;
	
	@Autowired
	private EmailService emailService;

	@Override
	public User register(RegisterRequest request) 
	{
	    if (userRepository.findByEmail(request.getEmail()).isPresent()) {
	        throw new RuntimeException("Email already exists");
	    }

	    User user = new User();
	    user.setName(request.getName());
	    user.setEmail(request.getEmail());
	    user.setPassword(passwordEncoder.encode(request.getPassword()));

	    if (request.getRole() == null) {
	        user.setRole("STUDENT");
	    } else {
	        user.setRole(request.getRole());
	    }

	    User savedUser = userRepository.save(user);
	    emailService.sendEmail(
	        savedUser.getEmail(),
	        "Registration Successful",
	        "Hello " + savedUser.getName() + ",\n\n"
	        + "You have successfully registered as " + savedUser.getRole() + "."
	    );

	    return savedUser;
	}

	@Override
	public String login(String email, String password, String role) {
		User user = userRepository.findByEmailAndRole(email, role)
				.orElseThrow(() -> new RuntimeException("User Not Found..!!"));
		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new RuntimeException("Invalid Password...!!");
		}
		return jwtutil.generateToken(user.getEmail(), user.getRole());
	}

}

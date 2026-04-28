package com.klef.fsad.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.klef.fsad.dto.RegisterRequest;
import com.klef.fsad.model.User;
import com.klef.fsad.repository.UserRepository;
import com.klef.fsad.security.JwtUtil;
import com.klef.fsad.service.GoogleService;
import com.klef.fsad.service.OtpService;
import com.klef.fsad.service.UserService;
import java.util.Map;
import org.springframework.http.ResponseEntity;



@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/")
public class UserController 
{
	@Autowired
	private UserService userService;
	@Autowired
	private GoogleService googleService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private OtpService otpService;
	
	@PostMapping("/register")
	public User register(@RequestBody RegisterRequest request)
	{
		return userService.register(request);
	}
	@PostMapping("/auth/google")
	public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> body) {
	    try {
	        String token = body.get("token");

	        var payload = googleService.verifyToken(token);

	        String email = payload.getEmail();
	        String name = (String) payload.get("name");

	        User user = userRepository.findByEmail(email)
	                .orElseGet(() -> {
	                    User newUser = new User();
	                    newUser.setEmail(email);
	                    newUser.setName(name);
	                    newUser.setPassword("GOOGLE_USER");
	                    newUser.setRole("STUDENT");
	                    return userRepository.save(newUser);
	                });

	        String jwt = jwtUtil.generateToken(user.getEmail(), user.getRole());

	        return ResponseEntity.ok(Map.of(
	                "token", jwt,
	                "user", user
	        ));

	    } catch (Exception e) {
	        return ResponseEntity.badRequest().body("Google login failed");
	    }
	}
	@PostMapping("/send-otp")
	public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> body) {

	    String email = body.get("email");

	    String response = otpService.sendOtp(email);

	    return ResponseEntity.ok(response);
	}
	@PostMapping("/verify-otp")
	public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> body) {

	    String email = body.get("email");
	    String otp = body.get("otp");

	    boolean isValid = otpService.verifyOtp(email, otp);

	    if (isValid) {
	        return ResponseEntity.ok("OTP Verified");
	    } else {
	        return ResponseEntity.badRequest().body("Invalid OTP");
	    }
	}
	
	

}

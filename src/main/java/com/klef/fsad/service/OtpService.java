package com.klef.fsad.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class OtpService {

    @Autowired
    private JavaMailSender mailSender;

    // temporary otp storage
    private Map<String, String> otpStorage = new HashMap<>();

    // send otp
    public String sendOtp(String email) {

        Random random = new Random();

        String otp = String.format("%06d", random.nextInt(999999));

        otpStorage.put(email, otp);

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("OTP Verification");
        message.setText("Your OTP is: " + otp);

        mailSender.send(message);

        return "OTP Sent Successfully";
    }

    // verify otp
    public boolean verifyOtp(String email, String otp) {

        String storedOtp = otpStorage.get(email);

        return storedOtp != null && storedOtp.equals(otp);
    }
}
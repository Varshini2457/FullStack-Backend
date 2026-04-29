package com.klef.fsad.service;

import com.google.api.client.googleapis.auth.oauth2.*;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class GoogleService {

    private static final String CLIENT_ID = "11180153759-5fhuog9334cns14c25d069aihgpo2hu7.apps.googleusercontent.com";

    public GoogleIdToken.Payload verifyToken(String token) throws Exception {

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                JacksonFactory.getDefaultInstance()
        )
        .setAudience(Collections.singletonList(CLIENT_ID))
        .build();

        GoogleIdToken idToken = verifier.verify(token);

        if (idToken != null) {
            return idToken.getPayload();
        } else {
            throw new RuntimeException("Invalid Google Token");
        }
    }
}
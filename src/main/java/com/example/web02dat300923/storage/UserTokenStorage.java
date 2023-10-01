package com.example.web02dat300923.storage;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
public class UserTokenStorage {
    private static final UserTokenStorage instance = new UserTokenStorage();

    private final Map<String, String> userTokens;

    private UserTokenStorage() {
        userTokens = new HashMap<>();
    }

    public static UserTokenStorage getInstance() {
        return instance;
    }

    public void addToken(String username, String token) {
        userTokens.put(username, token);
    }

    public String getToken(String username) {
        return userTokens.get(username);
    }

    public void removeToken(String username) {
        userTokens.remove(username);
    }
}

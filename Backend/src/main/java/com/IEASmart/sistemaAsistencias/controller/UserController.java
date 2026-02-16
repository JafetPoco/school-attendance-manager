package com.IEASmart.sistemaAsistencias.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/user")
public class UserController {

    @GetMapping("/me")
    public Map<String, Object> me(Authentication authentication) {
        Map<String, Object> result = new HashMap<>();

        if (authentication == null || !authentication.isAuthenticated()) {
            result.put("authenticated", false);
            return result;
        }

        Object principal = authentication.getPrincipal();
        result.put("authenticated", true);

        if (principal instanceof OAuth2User) {
            OAuth2User user = (OAuth2User) principal;
            result.put("name", user.getAttribute("name"));
            result.put("email", user.getAttribute("email"));
            result.put("picture", user.getAttribute("picture"));
        } else {
            result.put("principal", principal.toString());
        }

        return result;
    }
}





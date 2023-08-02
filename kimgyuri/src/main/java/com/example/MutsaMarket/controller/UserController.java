package com.example.MutsaMarket.controller;

import com.example.MutsaMarket.entity.user.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/users")
public class UserController {
    private final UserDetailsManager manager;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserDetailsManager manager, PasswordEncoder passwordEncoder) {
        this.manager = manager;
        this.passwordEncoder = passwordEncoder;
    }

    //회원가입 UI
    @GetMapping("/signup")
    public String signupForm() {
        return "signup-form";
    }

    //회원가입
    @PostMapping("/signup")
    public String signupPost(@RequestParam("username")String username, @RequestParam("password") String password, @RequestParam("password-check") String passwordCheck,
                             @RequestParam("phone")String phone, @RequestParam("email") String email, @RequestParam("address") String address) {
        if (username.isEmpty() || password.isEmpty()) {
            log.warn("아이디와 비밀번호는 필수 입력 항목입니다.");
            return "signup-form";
        }
        if (password.equals(passwordCheck)) {
            log.info("비밀번호가 일치합니다.");
            manager.createUser(CustomUserDetails.builder().username(username).password(passwordEncoder.encode(password)).phone(phone).email(email).address(address).build());
            return "redirect:/users/login";
        }
        log.warn("비밀번호가 일치하지 않습니다.");
        return "redirect:/users/signup?error";
    }

    //로그인 UI
    @GetMapping("/login")
    public String loginForm() {
        return "login-form";
    }

    // 로그인 성공 후 로그인 여부를 판단하기 위한 GetMapping
    @GetMapping("/my-profile")
    public String myProfile(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        log.info(userDetails.getUsername());
        log.info(userDetails.getEmail());
        return "my-profile";
    }
}

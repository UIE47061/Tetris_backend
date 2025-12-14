package tetris.demo.controller;

import tetris.demo.model.User;
import tetris.demo.repository.UserRepository;
import tetris.demo.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private UserRepository userRepository;
    @Autowired private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("帳號已存在");
        }
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "註冊成功"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginUser) {
        Optional<User> userOpt = userRepository.findByUsername(loginUser.getUsername());
        
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(loginUser.getPassword())) {
            User user = userOpt.get();
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
            
            String token = jwtUtil.generateToken(user.getUsername());
            return ResponseEntity.ok(Map.of(
                "token", token,
                "username", user.getUsername(),
                "lastLogin", user.getLastLogin() != null ? user.getLastLogin().toString() : null
            ));
        }
        return ResponseEntity.status(401).body("帳號或密碼錯誤");
    }
}
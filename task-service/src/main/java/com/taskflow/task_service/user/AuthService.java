package com.taskflow.task_service.user;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	
	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService,
			AuthenticationManager authenticationManager) {
		super();
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
		this.authenticationManager = authenticationManager;
	}
	
	public AuthResponse register(RegisterRequest request) {
        User user = new User(request.email(), passwordEncoder.encode(request.password()));
        userRepository.save(user);
        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        User user = userRepository.findByEmail(request.email())
            .orElseThrow(() -> new RuntimeException("User not found"));
        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }
	
	

}

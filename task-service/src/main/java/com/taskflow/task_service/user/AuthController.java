package com.taskflow.task_service.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;

	public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService,
			AuthenticationManager authenticationManager) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
		this.authenticationManager = authenticationManager;

	}
	
	@PostMapping("/register")
	public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request){
		User user = new User(request.email(), passwordEncoder.encode(request.password()));
		userRepository.save(user);
		String token = jwtService.generateToken(user);
		return ResponseEntity.ok(new AuthResponse(token));
	}
	
	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request){
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.email(), request.password())
				);
		User user = userRepository.findByEmail(request.email())
				.orElseThrow(() -> new RuntimeException("User not found"));
		String token = jwtService.generateToken(user);
		return ResponseEntity.ok(new AuthResponse(token));
	}
}

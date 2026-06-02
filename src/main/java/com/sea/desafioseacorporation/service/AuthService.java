package com.sea.desafioseacorporation.service;

import com.sea.desafioseacorporation.dto.request.LoginRequest;
import com.sea.desafioseacorporation.dto.request.RegisterRequest;
import com.sea.desafioseacorporation.dto.response.AuthResponse;
import com.sea.desafioseacorporation.dto.response.RegisterResponse;
import com.sea.desafioseacorporation.entity.Role;
import com.sea.desafioseacorporation.entity.UserEntity;
import com.sea.desafioseacorporation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public RegisterResponse register(RegisterRequest request) {
        String email = request.getEmail().trim().toLowerCase();
        String mensagemErro = "";
        if (userRepository.existsByEmail(email)) {
//            mensagemErro = "Email ja cadastrado.";
            throw new IllegalArgumentException("Email já cadastrado");
        }

        UserEntity user = new UserEntity();
        user.setName(request.getName().trim());
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.CLIENTE);
        user.setEnabled(true);

        UserEntity savedUser = userRepository.save(user);

        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
    }

    public AuthResponse login(LoginRequest request) {
        String email = request.getEmail().trim().toLowerCase();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Email ou senha inválidos"));

        if (!Boolean.TRUE.equals(user.getEnabled())) {
            throw new IllegalArgumentException("Usuário desativado");
        }

        boolean passwordMatches = passwordEncoder.matches(
                request.getPassword(),
                user.getPasswordHash()
        );

        if (!passwordMatches) {
            throw new IllegalArgumentException("Email ou senha inválidos");
        }

        String token = jwtService.generateToken(user);

        return new AuthResponse(
                "Login realizado com sucesso",
                token,
                "Bearer"
        );
    }
}
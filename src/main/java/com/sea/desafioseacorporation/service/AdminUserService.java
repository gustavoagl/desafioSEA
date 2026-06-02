package com.sea.desafioseacorporation.service;

import com.sea.desafioseacorporation.dto.request.CreateInternalUserRequest;
import com.sea.desafioseacorporation.dto.response.RegisterResponse;
import com.sea.desafioseacorporation.entity.Role;
import com.sea.desafioseacorporation.entity.UserEntity;
import com.sea.desafioseacorporation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterResponse createInternalUser(CreateInternalUserRequest request) {
        String email = request.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        if (request.getRole() == Role.CLIENTE) {
            throw new IllegalArgumentException("ADMIN não cria CLIENTE.");
        }

        UserEntity user = new UserEntity();
        user.setName(request.getName().trim());
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setEnabled(true);

        UserEntity savedUser = userRepository.save(user);

        return new RegisterResponse(
//                "",
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
    }
}
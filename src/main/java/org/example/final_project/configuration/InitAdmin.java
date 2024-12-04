package org.example.final_project.configuration;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.example.final_project.entity.RoleEntity;
import org.example.final_project.entity.UserEntity;
import org.example.final_project.repository.IRoleRepository;
import org.example.final_project.repository.IUserRepository;
import org.example.final_project.service.IUserService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.example.final_project.util.specification.RoleSpecification.isRole;
import static org.example.final_project.util.specification.UserSpecification.hasUsername;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InitAdmin {
    @NonFinal
    static final String ADMIN_EMAIL = "admin@gmail.com";
    @NonFinal
    static final String ADMIN_USERNAME = "admin";

    @NonFinal
    static final String ADMIN_PASSWORD = "admin";

    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(IUserRepository userRepository, IRoleRepository roleRepository) {
        return args -> {
            if (userRepository.findOne(Specification.where(hasUsername(ADMIN_USERNAME))).isEmpty()) {
                RoleEntity adminRole;
                if (roleRepository.findOne(Specification.where(isRole("ROLE_ADMIN"))).isPresent()) {
                    adminRole = roleRepository.findOne(Specification.where(isRole("ROLE_ADMIN"))).get();
                } else {
                    adminRole = RoleEntity.builder()
                            .roleName("ROLE_ADMIN")
                            .build();
                }
                roleRepository.save(adminRole);
                userRepository.save(UserEntity.builder()
                        .email(ADMIN_EMAIL)
                        .username(ADMIN_USERNAME)
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .role(adminRole)
                        .isActive(1)
                        .build());
            } else {
                UserEntity user =userRepository.findOne(Specification.where(hasUsername(ADMIN_USERNAME))).get();
                user.setPassword(passwordEncoder.encode(ADMIN_PASSWORD));
                userRepository.save(user);
            }
        };
    }
}

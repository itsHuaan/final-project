package org.example.final_project.service.impl;

import org.example.final_project.configuration.UserDetailsImpl;
import org.example.final_project.dto.UserDto;
import org.example.final_project.entity.RoleEntity;
import org.example.final_project.entity.UserEntity;
import org.example.final_project.mapper.UserMapper;
import org.example.final_project.model.UserModel;
import org.example.final_project.repository.IRoleRepository;
import org.example.final_project.repository.IUserRepository;
import org.example.final_project.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static org.example.final_project.util.specification.UserSpecification.*;

@Service
public class UserService implements IUserService, UserDetailsService {
    private final IRoleRepository roleRepository;
    private final IUserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(IRoleRepository roleRepository, IUserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream().map(userMapper::toDto).toList();
    }

    @Override
    public UserDto getById(Long id) {
        return userMapper.toDto(userRepository.getReferenceById(id));
    }

    @Override
    public int save(UserModel userModel) {
        if (isExistingByUsernameOrEmail(userModel.getUsername(), userModel.getEmail())) {
            return 0;
        }
        RoleEntity role = roleRepository.findById(userModel.getRoleId()).orElseThrow(() -> new IllegalArgumentException("Invalid role ID"));
        userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
        UserEntity userEntity = userMapper.toEntity(userModel);
        userEntity.setRole(role);
        userRepository.save(userEntity);
        return 1;
    }

    @Override
    public int delete(Long id) {
        return 0;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new UserDetailsImpl(userRepository.findOne(Specification.where(hasUsername(username)))
                .orElseThrow(() -> new UsernameNotFoundException("User not found")));
    }

    @Override
    public UserDto findByUsername(String username) {
        return userMapper.toDto(Objects.requireNonNull(userRepository.findOne(Specification.where(hasUsername(username))).orElse(null)));
    }

    @Override
    public boolean isExistingByUsernameOrEmail(String username, String email) {
        return userRepository.findOne(Specification.where(hasUsername(username).or(hasEmail(email)))).isPresent();
    }

    @Override
    public boolean isActivated(String username) {
        return userRepository.findOne(Specification.where(hasUsername(username)).and(isActive())).isPresent();
    }

    @Override
    public int activateUserAccount(String username, String email) {
        Specification<UserEntity> isExistingAndDeactivated = Specification.where(hasUsername(username).and(hasEmail(email)).and(isInactive()));
        if (userRepository.findOne(isExistingAndDeactivated).isPresent()) {
            UserEntity deactivatedAccount = userRepository.findOne(isExistingAndDeactivated).get();
            deactivatedAccount.setIsActive(1);
            userRepository.save(deactivatedAccount);
            return 1;
        }
        return 0;
    }
}

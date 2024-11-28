package org.example.final_project.service.impl;

import com.cloudinary.api.exceptions.NotFound;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.configuration.UserDetailsImpl;
import org.example.final_project.configuration.cloudinary.ImageService;
import org.example.final_project.dto.ApiResponse;

import org.example.final_project.dto.UserDto;
import org.example.final_project.entity.AddressEntity;
import org.example.final_project.entity.RoleEntity;
import org.example.final_project.entity.UserEntity;
import org.example.final_project.mapper.UserMapper;
import org.example.final_project.model.ProfileUpdateRequest;
import org.example.final_project.model.ShopRegisterRequest;
import org.example.final_project.model.UserModel;
import org.example.final_project.model.enum_status.STATUS;
import org.example.final_project.repository.IRoleRepository;
import org.example.final_project.repository.IUserRepository;
import org.example.final_project.service.IUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.example.final_project.dto.ApiResponse.createResponse;
import static org.example.final_project.util.specification.UserSpecification.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService implements IUserService, UserDetailsService {

    IRoleRepository roleRepository;
    IUserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    ImageService imageService;


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
            if(userRepository.findOne(Specification.where(isActive())).isPresent()){
                return 0;
            } else {
                UserEntity userEntity = userRepository.findOne(Specification.where(hasEmail(userModel.getEmail()).and(hasUsername(userModel.getUsername())))).get();
                userEntity.setPassword(passwordEncoder.encode(userModel.getPassword()));
                userRepository.save(userEntity);
                return 1;
            }
        }
        RoleEntity role = roleRepository.findById(userModel.getRoleId()).orElseThrow(() -> new IllegalArgumentException("Invalid role ID"));
        userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
        UserEntity userEntity = userMapper.toEntity(userModel);
        userEntity.setRole(role);
        userRepository.save(userEntity);
        return 1;
    }

    @Override
    public int update(Long aLong, UserModel userModel) {

        return 0;
    }

    @Override
    public int delete(Long id) {
        boolean isPresent = userRepository.existsById(id);
        if (isPresent) {
            UserEntity user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
            user.setDeletedAt(LocalDateTime.now());
            return 1;
        }
        return 0;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return new UserDetailsImpl(userRepository.findOne(Specification.where(hasEmail(email)))
                .orElseThrow(() -> new UsernameNotFoundException("User not found")));
    }

    @Override
    public UserDto findByUsername(String username) {
        return userMapper.toDto(Objects.requireNonNull(userRepository.findOne(Specification.where(hasUsername(username))).orElse(null)));
    }

    @Override
    public UserDto findByEmail(String email) {
        return userMapper.toDto(Objects.requireNonNull(userRepository.findOne(Specification.where(hasEmail(email))).orElse(null)));
    }

    @Override
    public boolean isExistingByUsernameOrEmail(String username, String email) {
        return userRepository.findOne(Specification.where(hasUsername(username)
                .or(hasEmail(email)))).isPresent();
    }

    @Override
    public boolean isActivated(String email) {
        return userRepository.findOne(Specification.where(hasEmail(email)).and(isActive())).isPresent();
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

    @Override
    public int activateUserAccount(String email) {
        Specification<UserEntity> isExistingAndDeactivated = Specification.where(hasEmail(email).and(isInactive()));
        if (userRepository.findOne(isExistingAndDeactivated).isPresent()) {
            UserEntity deactivatedAccount = userRepository.findOne(isExistingAndDeactivated).get();
            deactivatedAccount.setIsActive(1);
            userRepository.save(deactivatedAccount);
            return 1;
        }
        return 0;
    }

    @Override
    public int resetPassword(String email, String newPassword) {
        Specification<UserEntity> isExistingAndActivated = Specification.where(hasEmail(email).and(isActive()));
        if (userRepository.findOne(isExistingAndActivated).isPresent()) {
            UserEntity currentAccount = userRepository.findOne(isExistingAndActivated).get();
            currentAccount.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(currentAccount);
            return 1;
        }
        return 0;
    }

    @Override
    public int changePassword(String username, String oldPassword, String newPassword) {
        UserEntity userEntity = userRepository.findOne(Specification.where(hasUsername(username)).and(isActive())).isPresent()
                ? userRepository.findOne(Specification.where(hasUsername(username)).and(isActive())).get()
                : null;
        if (userEntity != null) {
            boolean isMatchWithOldPassword = passwordEncoder.matches(oldPassword, userEntity.getPassword());
            if (isMatchWithOldPassword) {
                userEntity.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(userEntity);
                return 1;
            } else {
                return 0;
            }
        } else {
            return -1;
        }
    }

    @Override
    public boolean validatePassword(String email, String newPassword) {
        UserEntity userEntity = userRepository.findOne(Specification.where(hasEmail(email)).and(isActive())).isPresent()
                ? userRepository.findOne(Specification.where(hasEmail(email)).and(isActive())).get()
                : null;
        if (userEntity != null) {
            String oldPassword = userEntity.getPassword();
            return oldPassword.equals(passwordEncoder.encode(newPassword));
        }
        return false;
    }

    @Override
    public Page<UserDto> findAllUsers(Pageable pageable) {
        Specification<UserEntity> specification = Specification.where(isNotDeleted().and(isNotSuperAdmin()));
        return userRepository.findAll(specification, pageable).map(userMapper::toDto);
    }


    @Override
    public ResponseEntity<?> signIn(String email, String password) {
        return null;
    }

    @Override
    public ApiResponse<?> registerForBeingShop(ShopRegisterRequest request) throws Exception {
        if (request.getUserId().describeConstable().isPresent()) {
            UserEntity userEntity = userRepository.findById(request.getUserId()).get();

            if (userEntity.getShop_status() == 0) {
                String id_back = imageService.uploadOneImage(request.getId_back());
                userEntity.setId_back(id_back);
                String id_front = imageService.uploadOneImage(request.getId_front());
                userEntity.setId_front(id_front);
                userEntity.setShop_name(request.getShop_name());
                userEntity.setTax_code(request.getTax_code());
                AddressEntity addressEntity = new AddressEntity();
                addressEntity.setId(request.getShop_address());
                userEntity.setAddress(addressEntity);
                userEntity.setShop_address_detail(request.getShop_address_detail());
                userRepository.save(userEntity);
                return createResponse(HttpStatus.OK, "Wait for confirm ",null);
            } else if (userEntity.getShop_status() == 1) {
                return createResponse(HttpStatus.CONFLICT, "User register Shop",null);
            }
        }
        throw new NotFound("Not found Userr");
    }
    @Override
    public ApiResponse<?> acceptfromAdmin(int status , long userId) throws Exception{
        Optional<UserEntity> optionalUserEntity = userRepository.findById(userId);
        if (optionalUserEntity.isPresent()) {
            UserEntity userEntity =  userRepository.findById(userId).get();
            userEntity.setShop_status(status);
            RoleEntity role = new RoleEntity();
            role.setRoleId(1L);
            userEntity.setRole(role);
            userRepository.save(userEntity);
            return createResponse(HttpStatus.OK, "Đã tạo shop ",null);

        }
        throw new NotFound("Not found Userr");
    }



    };

    @Override
    public int updateUserProfile(ProfileUpdateRequest request) {
        return 0;
    }
};


package org.example.final_project.service.impl;

import com.cloudinary.api.exceptions.NotFound;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.final_project.configuration.UserDetailsImpl;
import org.example.final_project.configuration.cloudinary.MediaUploadService;
import org.example.final_project.dto.ApiResponse;
import org.example.final_project.dto.ChatUserDto;
import org.example.final_project.dto.UserDto;
import org.example.final_project.entity.RoleEntity;
import org.example.final_project.entity.UserEntity;
import org.example.final_project.enumeration.ShopStatus;
import org.example.final_project.mapper.UserMapper;
import org.example.final_project.model.*;
import org.example.final_project.repository.IAddressRepository;
import org.example.final_project.repository.IRoleRepository;
import org.example.final_project.repository.IShippingAddressRepository;
import org.example.final_project.repository.IUserRepository;
import org.example.final_project.service.IAddressService;
import org.example.final_project.service.IUserService;
import org.example.final_project.specification.UserSpecification;
import org.example.final_project.util.EmailTemplate;
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

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.example.final_project.dto.ApiResponse.createResponse;
import static org.example.final_project.specification.UserSpecification.*;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService implements IUserService, UserDetailsService {

    IRoleRepository roleRepository;
    IUserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    MediaUploadService mediaUploadService;
    IAddressRepository addressRepository;
    IShippingAddressRepository shippingAddressRepository;
    IAddressService addressService;
    EmailService emailService;


    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream().map(userMapper::toDto).toList();
    }

    @Override
    public UserDto getById(Long id) {
        UserEntity user = userRepository.findOne(hasId(id).and(isNotDeleted()).and(isNotSuperAdmin())).orElse(null);
        return user != null ? userMapper.toDto(user) : null;
    }

    @Override
    public int save(UserModel userModel) {
        if ((userRepository.findOne(Specification.where(hasUsername(userModel.getUsername())).and(isActive().and(isNotDeleted()))).isPresent()) || (userRepository.findOne(Specification.where(hasEmail(userModel.getEmail())).and(isActive().and(isNotDeleted()))).isPresent())) {
            return 0;
        }
        Optional<UserEntity> inactiveOrDeletedUser = userRepository.findOne(Specification.where(
                hasEmail(userModel.getEmail())
                        .and(isInactive().or(isDeleted()))
        ));

        if (inactiveOrDeletedUser.isPresent()) {
            UserEntity userEntity = inactiveOrDeletedUser.get();
            userEntity.setName(userModel.getName());
            userEntity.setEmail(userModel.getEmail());
            userEntity.setUsername(userModel.getUsername());
            userEntity.setPassword(passwordEncoder.encode(userModel.getPassword()));
            userEntity.setDeletedAt(null);
            userEntity.setIsActive(0);
            userEntity.setShop_status(0);
            userRepository.save(userEntity);
            return 1;
        }

        RoleEntity role = roleRepository.findById(userModel.getRoleId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid role ID"));

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
        boolean isPresent = userRepository.findOne(hasId(id).and(isNotDeleted())).isPresent();
        if (isPresent) {
            UserEntity user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
            user.setDeletedAt(LocalDateTime.now());
            userRepository.save(user);
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
        return userRepository.findOne(Specification.where(
                        hasUsername(username)
                                .or(hasEmail(email))
                ).and(isActive().and(isNotDeleted()))
        ).isPresent();
    }

    @Override
    public boolean isActivated(String email) {
        return userRepository.findOne(Specification.where(hasEmail(email)).and(isActive()).and(isNotDeleted())).isPresent();
    }

    @Override
    public int activateUserAccount(String email) {
        Specification<UserEntity> isExistingAndDeactivated = Specification.where(hasEmail(email).and(isInactive().or(isDeleted())));
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
    public boolean validatePassword(String email, String password) {
        UserEntity userEntity = userRepository.findOne(Specification.where(hasEmail(email)).and(isActive())).isPresent()
                ? userRepository.findOne(Specification.where(hasEmail(email)).and(isActive())).get()
                : null;
        if (userEntity != null) {
            String oldPassword = userEntity.getPassword();
            return oldPassword.equals(passwordEncoder.encode(password));
        }
        return false;
    }

    @Override
    public Page<UserDto> findAllUsers(Pageable pageable, String name, Integer status) {
        Specification<UserEntity> spec = Specification.where(isNotDeleted().and(isNotSuperAdmin()));
        if (status != null) {
            spec = spec.and(hasStatus(status));
        }
        if (name != null) {
            spec = spec.and(containName(name));
        }
        return userRepository.findAll(spec, pageable).map(userMapper::toDto);
    }

    @Override
    public ApiResponse<?> registerForBeingShop(ShopRegisterRequest request) throws Exception {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(request.getUserId());
        long shopAddressId = request.getShop_address();
        boolean result = userRepository.existsByCitizenIdentification(request.getCitizenIdentification());
        if (result) {
            return createResponse(HttpStatus.CONFLICT, "Citizen Identification Is Existing", null);
        } else {
            if (optionalUserEntity.isPresent() || !addressRepository.existsById(shopAddressId)) {
                UserEntity userEntity = userRepository.findById(request.getUserId()).orElse(null);
                if (userEntity == null) {
                    throw new UsernameNotFoundException("User not found");
                }
                if (userRepository.existsByShopName(request.getShop_name())) {
                    return createResponse(HttpStatus.CONFLICT, "Shop Name Existed", null);
                }
                if (userEntity.getShop_status() == 0) {
                    String id_back = mediaUploadService.uploadOneImage(request.getId_back());
                    userEntity.setId_back(id_back);
                    String id_front = mediaUploadService.uploadOneImage(request.getId_front());
                    userEntity.setId_front(id_front);
                    userEntity.setCitizenIdentification(request.getCitizenIdentification());
                    userEntity.setShop_name(request.getShop_name());
                    userEntity.setTax_code(request.getTax_code());
                    userEntity.setAddress_id_shop(request.getShop_address());
                    userEntity.setShop_address_detail(request.getShop_address_detail());
                    userEntity.setPhone(request.getPhone());
                    userEntity.setTime_created_shop(LocalDateTime.now());
                    userEntity.setShop_status(ShopStatus.PENDING.getValue());
                    userRepository.save(userEntity);
                    return createResponse(HttpStatus.OK, "Waiting for confirmation", null);
                } else if (userEntity.getShop_status() == 1) {
                    return createResponse(HttpStatus.CONFLICT, "Shop is existing", null);
                } else if (userEntity.getShop_status() == 2) {
                    return createResponse(HttpStatus.CONFLICT, "Shop registration is Waiting", null);
                } else if (userEntity.getShop_status() == 3) {
                    return createResponse(HttpStatus.CONFLICT, "Shop registration is Refused", null);
                } else if (userEntity.getShop_status() == 4) {
                    return createResponse(HttpStatus.CONFLICT, "Shop Locked", null);
                }
            }
        }
        throw new NotFound("Not found User");
    }

    @Override
    public ApiResponse<?> acceptFromAdmin(long userId, LockShopRequest request) throws Exception {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(userId);
        if (optionalUserEntity.isEmpty()) {
            throw new NotFound("Not found User");
        }
        UserEntity userEntity = optionalUserEntity.get();
        userEntity.setShop_status(request.getStatus());
        RoleEntity role = new RoleEntity();
        switch (request.getStatus()) {
            case 1:
                role.setRoleId(1L);
                userEntity.setRole(role);
                userRepository.save(userEntity);
                return createResponse(HttpStatus.OK, "Shop Accepted", null);
            case 3:
            case 4:
                role.setRoleId(2L);
                userEntity.setRole(role);
                userRepository.save(userEntity);
                if (request.getStatus() == 4) {
                    EmailModel lockShop = new EmailModel(
                            userEntity.getEmail(),
                            "Your shop has been locked",
                            EmailTemplate.shopLockedEmailContent(request.getReason())
                    );
                    boolean result = emailService.sendEmail(lockShop);
                    return result
                            ? createResponse(HttpStatus.OK, "Shop Locked", null)
                            : createResponse(HttpStatus.BAD_REQUEST, "Failed to lock shop", null);
                }
                return createResponse(HttpStatus.OK, "Shop Rejected", null);
            default:
                return createResponse(HttpStatus.BAD_REQUEST, "Invalid Status", null);
        }
    }

    @Override
    public ResponseEntity<?> updateProfile(String username, ProfileUpdateRequest request) {
        UserEntity userEntity = userRepository.findOne(Specification.where(hasUsername(username)).and(isActive())).isPresent()
                ? userRepository.findOne(Specification.where(hasUsername(username)).and(isActive())).get()
                : null;

        if (userRepository.findOne(Specification.where(hasEmail(request.getEmail())).and(isActive())).isPresent()
                && !userEntity.getEmail().equals(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(createResponse(
                    HttpStatus.CONFLICT,
                    "Email " + request.getEmail() + " is already in use",
                    null
            ));
        }

        if (userEntity != null) {
            userEntity.setName(request.getName() != null ? request.getName() : userEntity.getName());
            userEntity.setPhone(request.getPhone() != null ? request.getPhone() : userEntity.getPhone());
            userEntity.setEmail(request.getEmail() != null ? request.getEmail() : userEntity.getEmail());
            userEntity.setGender(request.getGender() != null ? request.getGender() : userEntity.getGender());

            if (request.getProfilePicture() != null) {
                try {
                    userEntity.setProfilePicture(mediaUploadService.uploadSingleMediaFile(request.getProfilePicture()));
                } catch (IOException e) {
                    userEntity.setProfilePicture(userEntity.getProfilePicture() != null
                            ? userEntity.getProfilePicture()
                            : null);
                }
            }

            userRepository.save(userEntity);
            return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                    HttpStatus.OK,
                    "User updated",
                    null
            ));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createResponse(
                    HttpStatus.NOT_FOUND,
                    "User not found",
                    null
            ));
        }
    }

    @Override
    public ResponseEntity<?> changeAccountStatus(long userId, ChangeAccountStatusRequest request) {
        boolean isActivate = request.getStatus() == 1;
        Specification<UserEntity> specification = Specification.where(
                hasId(userId).and(isNotDeleted())
        );
        UserEntity userEntity = userRepository.findOne(specification).isPresent()
                ? userRepository.findOne(specification).get()
                : null;
        if (userEntity != null) {
            userEntity.setIsActive(request.getStatus());
            userEntity.setActivationNote(isActivate ? null : request.getNote());
            userRepository.save(userEntity);
            return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                    HttpStatus.OK,
                    isActivate ? "User activated" : "User deactivated",
                    null
            ));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createResponse(
                    HttpStatus.NOT_FOUND,
                    "User not found",
                    null
            ));
        }
    }

    @Override
    public Page<UserDto> getAllShop(Integer status, Pageable pageable){
        Specification<UserEntity> specification = UserSpecification.hasShopStatus(ShopStatus.ACTIVE.getValue());
        return userRepository.findAll(specification, pageable).map(userEntity -> {
            UserDto userDto = userMapper.toDto(userEntity);
            long parentId = userDto.getAddress_id_shop();
            List<String> address = addressService.findAddressNamesFromParentId(parentId);
            userDto.setAllAddresses(address);
            return userDto;
        });
    }

    @Override
    public List<UserDto> findByShopName(String shopName, Integer shopStatus) {
        List<UserEntity> userEntityList;
        if (shopName == null && shopStatus == null) {
            userEntityList = userRepository.findAll();
        } else if (shopName != null) {
            if (shopStatus == null) {
                userEntityList = userRepository.findByShopName(shopName);
            } else {
                userEntityList = userRepository.findByShopStatusAndName(shopStatus, shopName);
            }
        } else {
            userEntityList = userRepository.findByShopStatus(shopStatus);
        }
        return userEntityList.stream().map(userMapper::toDto).toList();

    }

    @Override
    public int updateShop(Long userId, ShopModel shopModel) {
        Optional<UserEntity> user = userRepository.findById(userId);
        if (user.isPresent()) {
            UserEntity userEntity = user.get();
            userEntity.setAddress_id_shop(shopModel.getShop_address());
            userEntity.setShop_name(shopModel.getShop_name());
            userEntity.setAddress_id_shop(shopModel.getShop_address());
            userRepository.save(userEntity);
            return 1;
        }
        return 0;

    }

    @Override
    public List<ChatUserDto> getChatUsers(Long senderId) {
        return null;
    }


    @Override
    public List<UserDto> findActiveUsers() {
        return userRepository.findAll(Specification.where(isActive().and(isNotSuperAdmin()))).stream().map(userMapper::toDto).toList();
    }
}



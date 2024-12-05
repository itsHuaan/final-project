package org.example.final_project.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.api.exceptions.BadRequest;
import com.cloudinary.api.exceptions.NotFound;
import com.cloudinary.utils.ObjectUtils;
import jakarta.persistence.EntityNotFoundException;
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
import org.example.final_project.entity.UserShippingAddressEntity;
import org.example.final_project.mapper.UserMapper;
import org.example.final_project.model.*;
import org.example.final_project.model.enum_status.STATUS;
import org.example.final_project.repository.IAddressRepository;
import org.example.final_project.repository.IRoleRepository;
import org.example.final_project.repository.IShippingAddressRepository;
import org.example.final_project.repository.IUserRepository;
import org.example.final_project.service.IAddressService;
import org.example.final_project.service.IShippingAddressService;
import org.example.final_project.service.IUserService;
import org.example.final_project.util.specification.UserSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import java.util.ArrayList;
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
    IAddressRepository addressRepository;
    IShippingAddressRepository shippingAddressRepository;

    Cloudinary cloudinary;
    IAddressService addressService;


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
        return userRepository.findOne(Specification.where(hasEmail(email)).and(isActive())).isPresent();
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
    public Page<UserDto> findAllUsers(Pageable pageable) {
        Specification<UserEntity> specification = Specification.where(isNotDeleted().and(isNotSuperAdmin()));
        return userRepository.findAll(specification, pageable).map(userMapper::toDto);
    }

    @Override
    public ApiResponse<?> registerForBeingShop(ShopRegisterRequest request) throws Exception {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(request.getUserId());
        long shopAddressId = request.getShop_address();

        if (optionalUserEntity.isPresent() || !addressRepository.existsById(shopAddressId)) {
            UserEntity userEntity = userRepository.findById(request.getUserId()).get();
            if (userEntity.getShop_status() == 0) {
                String id_back = imageService.uploadOneImage(request.getId_back());
                userEntity.setId_back(id_back);
                String id_front = imageService.uploadOneImage(request.getId_front());
                userEntity.setId_front(id_front);
                userEntity.setShop_name(request.getShop_name());
                userEntity.setTax_code(request.getTax_code());
                userEntity.setAddress_id_shop(request.getShop_address());
                userEntity.setShop_address_detail(request.getShop_address_detail());
                userEntity.setPhone(request.getPhone());
                userEntity.setTime_created_shop(LocalDateTime.now());
                userEntity.setShop_status(STATUS.INACTIVE.getStatus());
                userRepository.save(userEntity);
                return createResponse(HttpStatus.OK, "Wait for confirm ", null);
            } else if (userEntity.getShop_status() == 1) {
                return createResponse(HttpStatus.CONFLICT, "User register Shop", null);
            }
        }
        throw new NotFound("Not found Userr or Address");
    }

    @Override
    public ApiResponse<?> acceptFromAdmin(int status, long userId) throws Exception {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(userId);
        if (optionalUserEntity.isPresent()) {
            // status = 1 cho phép shop hoạt động
            if (status == 1) {
                UserEntity userEntity = userRepository.findById(userId).get();
                userEntity.setShop_status(status);
                RoleEntity role = new RoleEntity();
                role.setRoleId(1L);
                userEntity.setRole(role);
                userRepository.save(userEntity);
                return createResponse(HttpStatus.OK, "Created Shop", null);
                // status = 3 không cho phép hoạt động
            } else if (status == 3) {
                UserEntity userEntity = userRepository.findById(userId).get();
                userEntity.setShop_status(status);
                RoleEntity role = new RoleEntity();
                role.setRoleId(2L);
                userEntity.setRole(role);
                userRepository.save(userEntity);
                return createResponse(HttpStatus.OK, "Refuse  Shop", null);

            }
            // status = 4 Shop bị khóa
            else if (status == 4) {
                UserEntity userEntity = userRepository.findById(userId).get();
                userEntity.setShop_status(status);
                RoleEntity role = new RoleEntity();
                role.setRoleId(2L);
                userEntity.setRole(role);
                userRepository.save(userEntity);
                return createResponse(HttpStatus.OK, "Lock Shop", null);
            }
        }
        throw new NotFound("Not found Userr");
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
            userEntity.setGender(request.getGender() != -1 ? request.getGender() : userEntity.getGender());

            if (request.getProfilePicture() != null) {
                try {
                    String uploadedUrl = cloudinary.uploader().upload(
                            request.getProfilePicture().getBytes(),
                            ObjectUtils.emptyMap()
                    ).get("url").toString();
                    userEntity.setProfilePicture(uploadedUrl);
                } catch (IOException e) {
                    userEntity.setProfilePicture(null);
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
    public Page<UserDto> getAllShop(Integer status, Integer pageIndex, Integer pageSize) throws Exception {
        Specification<UserEntity> specification = UserSpecification.isShop();
        Pageable pageable = Pageable.unpaged();
        if (status != 0){
            specification = specification.and(hasShopStatus(status));
        }
        if (pageIndex != null && pageSize != null){
            if(pageIndex < 0){
                throw new BadRequest("Page index can not be less than 0");
            }
            if (pageSize <= 0) {
                throw new BadRequest("Page size can not be less than 0.");
            }
            pageable  = PageRequest.of(pageIndex, pageSize);
        }
        return userRepository.findAll(specification, pageable).map(userEntity -> {
            UserDto userDto = userMapper.toDto(userEntity);
            long parentId = userDto.getAddress_id_shop();
            List<String> address = addressService.findAddressNamesFromParentId(parentId);
            userDto.setAllAddresses(address);
            return userDto;
        });
    }

    @Override
    public int addAddress(long userId, AddShippingAddressRequest request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        AddressEntity address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new EntityNotFoundException("Address not found"));

        if (user.getShippingAddresses().size() >= 20) {
            throw new IllegalArgumentException("Cannot add more than 20 shipping addresses");
        }

        UserShippingAddressEntity newShippingAddress = UserShippingAddressEntity.builder()
                .user(user)
                .address(address)
                .addressLine2(request.getAddressDetail())
                .build();

        shippingAddressRepository.save(newShippingAddress);
        return 1;
    }
    @Override
    public List<UserDto> findByShopName(String shopName , Integer shopStatus) {
        List<UserEntity> userEntityList = new ArrayList<>();
        if (shopName == null && shopStatus == null ) {
            userEntityList = userRepository.findAll();
        }
         else if (shopName != null) {
            if(shopStatus == null){
                userEntityList = userRepository.findByShopName(shopName);
            }else {
                userEntityList = userRepository.findByShopStatusAndName(shopStatus, shopName);
            }
        }else {
            userEntityList = userRepository.findByShopStatus(shopStatus);
        }
        return userEntityList.stream().map(e->userMapper.toDto(e)).toList();

    }
    @Override
    public int updateShop( Long userId , ShopModel shopModel){
        Optional<UserEntity> user = userRepository.findById(userId);
        if(user.isPresent()){
            UserEntity userEntity = user.get();
            userEntity.setAddress_id_shop(shopModel.getShop_address());
            userEntity.setShop_name(shopModel.getShop_name());
            userEntity.setAddress_id_shop(shopModel.getShop_address());
            userRepository.save(userEntity);
            return 1;
        }
        return 0;

    }
}


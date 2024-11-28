package org.example.final_project.controller.admin;


import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.final_project.dto.UserDto;
import org.example.final_project.model.ShopRegisterRequest;
import org.example.final_project.service.impl.UserService;
import org.example.final_project.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User")
@RestController
@RequestMapping(value = Const.API_PREFIX + "/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(userService.getAll());
    }
    @PostMapping("/")
    @PreAuthorize("hasRole('ROLE_SELLER') or  hasRole('ROLE_BUYER')")
    public ResponseEntity<?> becomeShop(ShopRegisterRequest shopRegisterRequest) {
        UserDto userDto = userService.registerForBeingShop(shopRegisterRequest);
        return ResponseEntity.ok(userDto);
    }


}


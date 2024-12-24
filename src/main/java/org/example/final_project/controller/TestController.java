package org.example.final_project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.final_project.configuration.UserDetailsImpl;
import org.example.final_project.util.Const;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

import static org.example.final_project.dto.ApiResponse.createResponse;

@Tag(name = "Test")
@RestController
@RequestMapping(value = Const.API_PREFIX + "/test")
public class TestController {
    @Operation(summary = "Admin")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    @GetMapping("/admin-test")
    public ResponseEntity<?> adminTest() {
        return new ResponseEntity<>("You're an seller", HttpStatus.OK);
    }

    @Operation(summary = "User")
    @PreAuthorize("hasRole('ROLE_BUYER')")
    @GetMapping("/user-test")
    public ResponseEntity<?> userTest() {
        return new ResponseEntity<>("You're an buyer", HttpStatus.OK);
    }


    @Operation(summary = "Test")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/login-test")
    public ResponseEntity<?> loginTest() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal() instanceof UserDetailsImpl userDetails) {
            String username = userDetails.getUsername();
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            return new ResponseEntity<>("You're logged in as: " + username + " with roles: " + authorities, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Unable to retrieve user information.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Response Test")
    @GetMapping("/response-test")
    public ResponseEntity<?> responseTest() {
        return ResponseEntity.status(HttpStatus.OK).body(
                createResponse(
                        HttpStatus.NO_CONTENT,
                        "No content",
                        null
                )
        );
    }
}

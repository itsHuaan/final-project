package org.example.final_project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.final_project.util.Const;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Test")
@RestController
@RequestMapping(value = Const.API_PREFIX + "/test")
public class TestController {
    @Operation(summary = "Admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/admin-test")
    public ResponseEntity<?> adminTest() {
        return new ResponseEntity<>("You're an admin", HttpStatus.OK);
    }

    @Operation(summary = "User")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping("/user-test")
    public ResponseEntity<?> userTest() {
        return new ResponseEntity<>("You're an user", HttpStatus.OK);
    }
}

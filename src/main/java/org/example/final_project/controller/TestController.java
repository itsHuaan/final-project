package org.example.final_project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.configuration.UserDetailsImpl;
import org.example.final_project.dto.RevenueAndGrowthRateStatisticDto;
import org.example.final_project.service.IProductService;
import org.example.final_project.service.impl.StatisticService;
import org.example.final_project.util.Const;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

import static org.example.final_project.dto.ApiResponse.createResponse;

@Tag(name = "Test")
@RestController
@RequestMapping(value = Const.API_PREFIX + "/test")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TestController {
    IProductService productService;
    StatisticService statisticService;

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
    public ResponseEntity<?> responseTest(@RequestParam List<Long> addressIds) {
//        List<Long> ids = productService.getAllChildLocationIds(addressIds);
        return ResponseEntity.status(HttpStatus.OK).body(
                createResponse(
                        HttpStatus.OK,
                        "No content",
                        null
                )
        );
    }
    @Operation(summary = "Get super admin revenue")
    @GetMapping("/revenue-statistic")
    public ResponseEntity<?> getRevenue(@RequestParam int year) {
        RevenueAndGrowthRateStatisticDto revenueStatistics = statisticService.getRevenueStatisticsTest(year);
        HttpStatus status = revenueStatistics != null ? HttpStatus.OK : HttpStatus.NO_CONTENT;
        String message = revenueStatistics != null ? "Revenue fetched" : "No revenue data";
        return ResponseEntity.status(HttpStatus.OK).body(
                createResponse(
                        status,
                        message,
                        revenueStatistics
                )
        );
    }

    @Operation(summary = "Get seller revenue")
    @GetMapping("{shopId}/revenue-statistic")
    public ResponseEntity<?> getRevenue(@PathVariable Long shopId,
                                        @RequestParam int year) {
        RevenueAndGrowthRateStatisticDto revenueStatistics = statisticService.getRevenueStatisticsTest(shopId, year);
        HttpStatus status = revenueStatistics != null ? HttpStatus.OK : HttpStatus.NO_CONTENT;
        String message = revenueStatistics != null ? "Revenue fetched" : "No revenue data";
        return ResponseEntity.status(HttpStatus.OK).body(
                createResponse(
                        status,
                        message,
                        revenueStatistics
                )
        );
    }
}

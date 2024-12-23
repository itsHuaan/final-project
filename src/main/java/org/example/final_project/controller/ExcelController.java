package org.example.final_project.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.service.impl.ExportExcelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExcelController {
    ExportExcelService exportExcelService;

    @GetMapping("/excel/{shopId}")
    public ResponseEntity<?> readExcel(@PathVariable("shopId") Long shopId, HttpServletResponse response) throws IOException {
        exportExcelService.exportOrderToExcel(shopId, response);
        return ResponseEntity.ok("đã gửi");
    }
}

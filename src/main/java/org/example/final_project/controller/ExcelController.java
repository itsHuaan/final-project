package org.example.final_project.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.service.impl.ExcelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExcelController {
    ExcelService excelService;

    @GetMapping("/excel/{shopId}")
    public ResponseEntity<String> exportExcel(@PathVariable("shopId") Long shopId, HttpServletResponse response,
                                              @RequestParam String startTime,
                                              @RequestParam String endTime
    ) throws IOException {
        LocalDate startDate = LocalDate.parse(startTime);
        LocalDate endDate = LocalDate.parse(endTime);
        excelService.exportOrderToExcel(shopId, response, startDate, endDate);
        return ResponseEntity.ok("đã gửi");
    }

    @PostMapping("/import")
    public ResponseEntity<String> importExcel(@RequestParam("file") MultipartFile file) throws IOException {
        excelService.importExcel(file);
        return ResponseEntity.ok("import thành công");
    }
}

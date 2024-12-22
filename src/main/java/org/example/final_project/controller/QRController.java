package org.example.final_project.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.service.impl.QrService;
import org.example.final_project.util.Const;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping(Const.API_PREFIX + "/qr")

public class QRController {
    QrService qrService;

    @PostMapping("/scan")
    public ResponseEntity<?> scanQRCode(@RequestParam("file") MultipartFile file) {
        try {
            return ResponseEntity.ok(qrService.getUserInfo(file));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("căn cước không hợp lệ ");
        }
    }
}

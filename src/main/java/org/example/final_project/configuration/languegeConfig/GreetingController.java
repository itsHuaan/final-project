package org.example.final_project.configuration.languegeConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
public class GreetingController {

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/greet")
    public String greet(@RequestParam(value = "lang", defaultValue = "en") String lang) {
        // Thiết lập ngôn ngữ từ tham số "lang" trong URL
        LocaleContextHolder.setLocale(new Locale(lang));

        // Trả về thông điệp theo ngôn ngữ hiện tại
        return messageSource.getMessage("greeting", null, LocaleContextHolder.getLocale());
    }
}

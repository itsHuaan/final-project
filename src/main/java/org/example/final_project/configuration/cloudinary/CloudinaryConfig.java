package org.example.final_project.configuration.cloudinary;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.cloudinary.utils.ObjectUtils;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name","tuan2202",
                "api_key", "115772253396781",
                "api_secret", "uEDJ7WeK4H1PYKiYh0HAwnAixG0"// API Secret của bạn
        ));
    }
}



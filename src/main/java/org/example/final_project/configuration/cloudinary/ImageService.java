package org.example.final_project.configuration.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ImageService {
    @Autowired
    private Cloudinary cloudinary;

    public String uploadImage(MultipartFile[] file) throws IOException {
        List<String> imagesUrl = new ArrayList<>();
        for (MultipartFile img : file) {
            // Upload từng ảnh lên Cloudinary
            Map<String, Object> uploadResult = cloudinary.uploader().upload(img.getBytes(), ObjectUtils.emptyMap());

            // Lấy URL của ảnh đã upload và thêm vào danh sách
            String imageUrl = (String) uploadResult.get("url");
            imagesUrl.add(imageUrl);
        }
        return imagesUrl.toString();
    }
    public String uploadOneImage(MultipartFile file) throws IOException {
        byte[] fileBytes = file.getBytes();
        Map uploadResult = cloudinary.uploader().upload(fileBytes, ObjectUtils.emptyMap());
        return (String) uploadResult.get("url");
    }

}

package org.example.final_project.configuration.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MediaUploadService {
    Cloudinary cloudinary;

    public String uploadOneImage(MultipartFile file) throws IOException {
        byte[] fileBytes = file.getBytes();
        Map uploadResult = cloudinary.uploader().upload(fileBytes, ObjectUtils.emptyMap());
        return (String) uploadResult.get("url");
    }

    public List<String> uploadMultipleMediaFiles(MultipartFile[] files) throws IOException {
        List<String> mediaUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            Map<String, Object> uploadResult;
            String fileType = file.getContentType();
            if (fileType != null && fileType.startsWith("video")) {
                uploadResult = cloudinary.uploader().upload(file.getBytes(),
                        ObjectUtils.asMap("resource_type", "video"));
            } else {
                uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            }
            String mediaUrl = (String) uploadResult.get("url");
            mediaUrls.add(mediaUrl);
        }
        return mediaUrls;
    }

    public String uploadSingleMediaFile(MultipartFile file) throws IOException {
        Map<String, Object> uploadResult;
        String fileType = file.getContentType();
        if (fileType != null && fileType.startsWith("video")) {
            uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("resource_type", "video"));
        } else {
            uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        }
        return (String) uploadResult.get("url");
    }
}

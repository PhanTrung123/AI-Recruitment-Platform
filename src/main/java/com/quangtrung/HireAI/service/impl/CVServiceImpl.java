package com.quangtrung.HireAI.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.quangtrung.HireAI.entity.CV;
import com.quangtrung.HireAI.entity.User;
import com.quangtrung.HireAI.repository.CVRepository;
import com.quangtrung.HireAI.service.CVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class CVServiceImpl implements CVService {

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private CVRepository cvRepository;

    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    @Override
    public CV uploadCV(MultipartFile file, User user) throws IOException {
        try {
            Map<String, Object> uploadParams = ObjectUtils.asMap(
                    "resource_type", "auto",
                    "folder", "cvs", "access_mode", "public" // <-- Thêm dòng này để cấp quyền public"access_mode", "public" // <-- Thêm dòng này để cấp quyền public
            );

            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadParams);

            String fileUrl = (String) uploadResult.get("secure_url");

            CV cv = new CV();
            cv.setFileUrl(fileUrl);
            cv.setUser(user);
            cv.setCreatedAt(LocalDateTime.now());

            return cvRepository.save(cv);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload CV to Cloudinary", e);
        }
    }

    @Override
    public Optional<CV> getLatestCVByUser(User user) {
        return cvRepository.findFirstByUserOrderByCreatedAtDesc(user);
    }

    @Override
    public List<CV> getCVsByUser(User user) {
        return cvRepository.findByUser(user);
    }

    @Override
    public void deleteCV(Long id) {
        cvRepository.deleteById(id);
    }
}

package com.quangtrung.HireAI.service;

import com.quangtrung.HireAI.entity.CV;
import com.quangtrung.HireAI.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface CVService {
    CV uploadCV(MultipartFile file, User user) throws IOException;

    Optional<CV> getLatestCVByUser(User user);

    List<CV> getCVsByUser(User user);

    void deleteCV(Long id);
}

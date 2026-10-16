package com.quangtrung.HireAI.service;

import com.quangtrung.HireAI.dto.AccountDTO;
import com.quangtrung.HireAI.dto.LoginDTO;
import com.quangtrung.HireAI.dto.UserDTO;
import com.quangtrung.HireAI.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface UserService {

    // lưu theo accountDTO (đang ký tài khoản)
    public User save(AccountDTO accountDTO);

    // lưu đầy đủ thông tin user theo UserDTO (admin tạo)
    public User saveFullInfo(UserDTO userDTO);

    public User login(LoginDTO LoginDTO);

    boolean existsByEmail(String email);

    Page<User> findPaginated(Pageable pageable);

    Page<User> findByUserNameContaining(String name, Pageable pageable);

    Optional<User> findById(Long id);

    void updateProfile(Long id, UserDTO userDTO, MultipartFile avatarFile, boolean deleteAvatar);

    void deleteById(Long id);
}

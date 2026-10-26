package com.quangtrung.HireAI.service;

import com.quangtrung.HireAI.dto.AccountDTO;
import com.quangtrung.HireAI.dto.LoginDTO;
import com.quangtrung.HireAI.dto.UserDTO;
import com.quangtrung.HireAI.entity.User;
import com.quangtrung.HireAI.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface UserService {

    // lưu theo accountDTO (đang ký tài khoản)
    public User save(AccountDTO accountDTO);

    // lưu theo accountDTO với role cụ thể
    public User save(AccountDTO accountDTO, Role role);

    // lưu đầy đủ thông tin user theo UserDTO (admin tạo)
    public User saveFullInfo(UserDTO userDTO);

    public User login(LoginDTO LoginDTO);

    boolean existsByEmail(String email);

    Page<User> findPaginated(Pageable pageable);

    Page<User> findByUserNameContaining(String name, Pageable pageable);

    Optional<User> findById(Long id);

    Optional<User> findByUserName(String userName);

    Optional<User> findByEmail(String email);

    default User findByUsername(String username) {
        return findByUserName(username).orElse(null);
    }

    default User findUserByEmail(String email) {
        return findByEmail(email).orElse(null);
    }

    void updateProfile(Long id, UserDTO userDTO, MultipartFile avatarFile, boolean deleteAvatar);

    void deleteById(Long id);

    List<User> findByRole(Role role);

    void updateRole(Long userId, Role role);
}

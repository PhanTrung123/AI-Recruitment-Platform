package com.quangtrung.HireAI.service.impl;

import com.quangtrung.HireAI.dto.AccountDTO;
import com.quangtrung.HireAI.dto.LoginDTO;
import com.quangtrung.HireAI.dto.UserDTO;
import com.quangtrung.HireAI.entity.User;
import com.quangtrung.HireAI.enums.Role;
import com.quangtrung.HireAI.repository.UserRepository;
import com.quangtrung.HireAI.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private static final String UPLOAD_DIR = "C:/uploads/avatars/";

    @Override
    public User save(AccountDTO accountDTO) {
        User user = new User();
        BeanUtils.copyProperties(accountDTO, user);
        user.setPassword(bCryptPasswordEncoder.encode(accountDTO.getPassword()));
        user.setRole(Role.USER);
        return userRepository.save(user);
    }

    @Override
    public User saveFullInfo(UserDTO userDTO) {
        User user;
        if (userDTO.getId() != null) {
            user = userRepository.findById(userDTO.getId()).orElse(new User());
        } else {
            user = new User();
        }
        BeanUtils.copyProperties(
                userDTO,
                user,
                "avatarFile",
                "password",
                "avatar",
                "dateOfBirth"
        );
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            if (userDTO.getPassword().length() < 6) {
                throw new IllegalArgumentException("Mật khẩu phải từ 6 đến 100 ký tự");
            }
            user.setPassword(
                    bCryptPasswordEncoder.encode(userDTO.getPassword())
            );
        } else if (userDTO.getId() == null) {
            user.setPassword(
                    bCryptPasswordEncoder.encode("123456")
            );
        }
        if (userDTO.getAvatarFile() != null
                && !userDTO.getAvatarFile().isEmpty()) {

            try {
                String originalFilename = userDTO.getAvatarFile().getOriginalFilename();
                String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String newFilename = UUID.randomUUID().toString() + extension;

                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(newFilename);
                userDTO.getAvatarFile().transferTo(filePath.toFile());

                user.setAvatar("/uploads/" + newFilename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (userDTO.getDateOfBirth() != null) {
            user.setDateOfBirth(userDTO.getDateOfBirth());
        }
        if (userDTO.getRole() != null) {
            user.setRole(userDTO.getRole());
        } else {
            user.setRole(Role.USER);
        }
        return userRepository.save(user);
    }

    @Override
    public User login(LoginDTO loginDTO) {
        Optional<User> opt = userRepository.findByEmail(loginDTO.getEmail());

        if (opt.isPresent() && bCryptPasswordEncoder.matches(loginDTO.getPassword(), opt.get().getPassword())) {
            return opt.get();
        }
        return null;
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Page<User> findPaginated(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<User> findByUserNameContaining(String name, Pageable pageable) {
        return userRepository.findByUserNameContaining(name, pageable);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }


    @Override
    @Transactional
    public void updateProfile(Long id, UserDTO userDTO, MultipartFile avatarFile, boolean deleteAvatar) {
        Optional<User> opt = userRepository.findById(id);
        if (opt.isPresent()) {
            User user = opt.get();
            if (userDTO.getUserName() != null) {
                user.setUserName(userDTO.getUserName());
                System.out.println("Updated userName to: " + userDTO.getUserName());
            }
            if (userDTO.getPhone() != null) {
                user.setPhone(userDTO.getPhone());
                System.out.println("Updated phone to: " + userDTO.getPhone());
            }
            if (userDTO.getDateOfBirth() != null) {
                user.setDateOfBirth(userDTO.getDateOfBirth());
                System.out.println("Updated dateOfBirth to: " + userDTO.getDateOfBirth());
            }
            if (userDTO.getGender() != null) {
                user.setGender(userDTO.getGender());
                System.out.println("Updated gender to: " + userDTO.getGender());
            }
            if (userDTO.getAddress() != null) {
                user.setAddress(userDTO.getAddress());
                System.out.println("Updated address to: " + userDTO.getAddress());
            }
            if (userDTO.getPersonalLink() != null) {
                user.setPersonalLink(userDTO.getPersonalLink());
                System.out.println("Updated personalLink to: " + userDTO.getPersonalLink());
            }

            if (deleteAvatar) {
                user.setAvatar(null);
            } else if (avatarFile != null && !avatarFile.isEmpty()) {
                try {
                    String originalFilename = avatarFile.getOriginalFilename();
                    String sanitizedFilename = System.currentTimeMillis() + "_" + originalFilename.replaceAll("[^a-zA-Z0-9._-]", "_");
                    String fileName = sanitizedFilename;
                    String filePath = UPLOAD_DIR + fileName;
                    Path path = java.nio.file.Paths.get(filePath);
                    Files.copy(avatarFile.getInputStream(), path, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    user.setAvatar("/uploads/" + fileName);
                } catch (Exception e) {
                    throw new RuntimeException("Lỗi khi upload avatar: " + e.getMessage());
                }
            }
            userRepository.save(user);
        } else {
            throw new RuntimeException("Không tìm thấy người dùng với ID: " + id);
        }
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }


}

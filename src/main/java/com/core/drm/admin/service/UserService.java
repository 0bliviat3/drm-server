package com.core.drm.admin.service;

import com.core.drm.admin.domain.User;
import com.core.drm.admin.dto.UserDTO;
import com.core.drm.admin.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.core.drm.base.constant.DataStateCode.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User saveUser(UserDTO userDTO) {
        User user = User.builder()
                .userId(userDTO.getUserId())
                .name(userDTO.getName())
                .password(userDTO.getPassword())
                .createTime(LocalDateTime.now())
                .dataCode(I)
                .passwordSalt(userDTO.getPasswordSalt())
                .build();

        return userRepository.save(user);
    }

    public User findById(String userId) {
        return userRepository.findByIdAndDataCodeNot(userId, D)
                .orElseThrow(EntityNotFoundException::new);
    }

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAllByDataCodeNot(pageable, D);
    }

    @Transactional
    public User modifyUser(UserDTO userDTO) {
        User user = findById(userDTO.getUserId());
        String password = Optional.ofNullable(userDTO.getPassword())
                .orElse(user.getPassword());
        String name = Optional.ofNullable(userDTO.getName())
                .orElse(user.getName());
        String passwordSalt = Optional.ofNullable(userDTO.getPasswordSalt())
                .orElse(user.getPasswordSalt());
        user.setPassword(password);
        user.setPasswordSalt(passwordSalt);
        user.setName(name);
        user.setModifiedTime(LocalDateTime.now());
        user.setDataCode(U);
        return user;
    }

    @Transactional
    public User deleteUser(UserDTO userDTO) {
        User user = findById(userDTO.getUserId());
        user.setModifiedTime(LocalDateTime.now());
        user.setDataCode(D);
        return user;
    }

}

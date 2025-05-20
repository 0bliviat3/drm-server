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
                .userId(userDTO.userId())
                .name(userDTO.name())
                .password(userDTO.password())
                .createTime(LocalDateTime.now())
                .dateCode(I)
                .build();

        return userRepository.save(user);
    }

    public User findById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(EntityNotFoundException::new);
    }

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Transactional
    public User modifyUser(UserDTO userDTO) {
        User user = findById(userDTO.userId());
        String password = Optional.ofNullable(userDTO.password())
                .orElse(user.getPassword());
        String name = Optional.ofNullable(userDTO.name())
                .orElse(user.getName());
        user.setPassword(password);
        user.setName(name);
        user.setModifiedTime(LocalDateTime.now());
        user.setDateCode(U);
        return user;
    }

    @Transactional
    public User deleteUser(UserDTO userDTO) {
        User user = findById(userDTO.userId());
        user.setModifiedTime(LocalDateTime.now());
        user.setDateCode(D);
        return user;
    }

}

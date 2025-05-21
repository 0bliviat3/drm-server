package com.core.drm.admin.service;

import com.core.drm.admin.domain.User;
import com.core.drm.admin.dto.UserDTO;
import com.core.drm.admin.exception.UserException;
import com.core.drm.crypto.util.PropertiesUtil;
import lombok.RequiredArgsConstructor;
import org.postgresql.util.PasswordUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import static com.core.drm.admin.constant.UserExceptionMessage.*;

@Service
@RequiredArgsConstructor
public class SignService {

    private static final int SALT_SIZE = 16;

    private final UserService userService;


    private void validateHash(UserDTO userDTO) {
        String password = userDTO.getPassword();
        String salt = userDTO.getPasswordSalt();
        if (password == null || salt == null) {
            throw new UserException(FAIL_HASH_PASSWORD);
        }
    }

    private String hashPassword(UserDTO userDTO) {
        validateHash(userDTO);
        char[] password = userDTO.getPassword().toCharArray();
        int iteration = Integer.parseInt(PropertiesUtil.getApplicationProperty("password.iteration"));
        byte[] salt = userDTO.getPasswordSalt().getBytes();

        return PasswordUtil.encodeScramSha256(password, iteration, salt);
    }

    private String generateSalt() {
        try {
            return Base64.getEncoder()
                    .encodeToString(SecureRandom.getInstanceStrong().generateSeed(SALT_SIZE));
        } catch (NoSuchAlgorithmException e) {
            throw new UserException(FAIL_CREATE_SALT, e);
        }

    }

    public boolean isExistUserId(String userId) {
        try {
            userService.findById(userId);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean verifyUser(UserDTO userDTO) {
        User user = userService.findById(userDTO.getUserId());
        return MessageDigest.isEqual(
                hashPassword(userDTO).getBytes(),
                user.getPassword().getBytes());
    }

    private void saveUser(UserDTO userDTO) {
        String salt = generateSalt();
        String hashWord = hashPassword(
                UserDTO.builder()
                        .password(userDTO.getPassword())
                        .passwordSalt(salt)
                        .build()
        );
        userService.saveUser(
                UserDTO.builder()
                        .userId(userDTO.getUserId())
                        .password(hashWord)
                        .name(userDTO.getName())
                        .passwordSalt(salt)
                        .build()
        );
    }

    public void signUp(UserDTO userDTO) {
        if (isExistUserId(userDTO.getUserId())) {
            throw new UserException(USED_ID);
        }
        saveUser(userDTO);
    }

    private void validateSignIn(UserDTO userDTO) {
        validateId(userDTO.getUserId());
        validatePass(userDTO);
    }

    private void validateId(String userId) {
        if (!isExistUserId(userId)) {
            throw new UserException(INVALID_ID);
        }
    }

    private void validatePass(UserDTO userDTO) {
        if (!verifyUser(userDTO)) {
            throw new UserException(INVALID_PASSWORD);
        }
    }

    public UserDTO signIn(UserDTO userDTO) {
        validateSignIn(userDTO);
        User user = userService.findById(userDTO.getUserId());
        return user.toDTOWithoutPassWord();
    }

    /*
    [로그인 보안 정책]
    수정, 삭제시엔 비밀번호를 다시 입력받도록 한다.
    또한 삭제시 현재 세션인 경우 세션에서 제거할수 있도록 dto를 리턴해준다.
     */
    public UserDTO modifyUser(UserDTO userDTO) {
        validateSignIn(userDTO);
        User user = userService.modifyUser(userDTO);
        return user.toDTOWithoutPassWord();
    }

    public UserDTO deleteUser(UserDTO userDTO) {
        validateSignIn(userDTO);
        return userService.deleteUser(userDTO)
                .toDTOWithoutPassWord();
    }

    public Page<UserDTO> findAll(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return userService.findAll(pageable).map(User::toDTOWithoutPassWord);
    }

}

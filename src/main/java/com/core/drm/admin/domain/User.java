package com.core.drm.admin.domain;

import com.core.drm.admin.dto.UserDTO;
import com.core.drm.base.constant.DataStateCode;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "t_admin_user")
@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    @Column(name = "modified_time")
    private LocalDateTime modifiedTime;

    @Column(name = "data_code")
    @Enumerated(EnumType.STRING)
    private DataStateCode dataCode;

    @Column(name = "password_salt")
    private String passwordSalt;

    @Transient
    public UserDTO toDTOWithoutPassWord() {
        return UserDTO.builder()
                .userId(userId)
                .name(name)
                .createTime(createTime)
                .modifiedTime(modifiedTime)
                .build();
    }
}

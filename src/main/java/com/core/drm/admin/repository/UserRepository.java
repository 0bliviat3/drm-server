package com.core.drm.admin.repository;

import com.core.drm.admin.domain.User;
import com.core.drm.base.constant.DataStateCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUserIdAndDataCodeNot(String userId, DataStateCode dataStateCode);
    Page<User> findAllByDataCodeNot(Pageable pageable, DataStateCode dataStateCode);
}

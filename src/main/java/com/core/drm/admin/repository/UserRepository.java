package com.core.drm.admin.repository;

import com.core.drm.admin.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}

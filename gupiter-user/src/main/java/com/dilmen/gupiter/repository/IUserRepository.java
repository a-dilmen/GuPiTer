package com.dilmen.gupiter.repository;

import com.dilmen.gupiter.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAuthRepository extends JpaRepository<User,Long> {
    Optional<User> findOptionalByEmailAndPassword(String email, String password);

    Optional<User> findOptionalByEmail(String email);
}

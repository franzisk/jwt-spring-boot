package com.safeplace.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.safeplace.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findById(Long id);

	Optional<User> findByUsername(String username);

}

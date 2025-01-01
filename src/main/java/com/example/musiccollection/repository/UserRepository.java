package com.example.musiccollection.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.musiccollection.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{
    Optional <User> findByUserEmail(String userEmail);
    public Page<User> findByUserNameLikeOrFuriganaLike(String userNameKeyword, String furiganaKeyword, Pageable pageable);
    
    Optional <User> findByUserId(Integer userId);
}

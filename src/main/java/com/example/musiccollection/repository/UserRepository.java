package com.example.musiccollection.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.musiccollection.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{
    public User findByUserEmail(String userEmail);
    public Page<User> findByUserNameLikeOrFuriganaLike(String userNameKeyword, String furiganaKeyword, Pageable pageable);
}

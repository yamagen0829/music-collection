package com.example.musiccollection.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.musiccollection.entity.User;
import com.example.musiccollection.entity.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository< VerificationToken, Integer>{
   public VerificationToken findByToken(String Token);
   public VerificationToken findByUser(User user);
}

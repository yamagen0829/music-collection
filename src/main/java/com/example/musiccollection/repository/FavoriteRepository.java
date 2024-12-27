package com.example.musiccollection.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.example.musiccollection.entity.Favorite;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer>{
        Optional<Favorite> findByUser_UserIdAndMusic_MusicId(Integer userId, Integer musicId);
	
	    boolean existsByUser_UserIdAndMusic_MusicId(Integer userId, Integer musicId);
	
	    Page<Favorite> findByUser_UserId(@Param("userId") Integer userId, Pageable pageable);
}

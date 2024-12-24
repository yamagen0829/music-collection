package com.example.musiccollection.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.musiccollection.entity.Favorite;
import com.example.musiccollection.entity.Music;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer>{
        Optional<Favorite> findByUserIdAndMusicId(Integer userId, Integer musicId);
	
	    boolean existsByUserIdAndMusicId(Integer userId, Integer musicId);
	
	    @Query("SELECT f.music FROM favorite f WHERE f.user.userId = :userId")
	    Page<Music> findByFavoriteByUserId(@Param("userId") Integer userId, Pageable pageable);
}

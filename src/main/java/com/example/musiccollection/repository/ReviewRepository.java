package com.example.musiccollection.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.example.musiccollection.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer>{
	List<Review> findTop6ByMusicMusicIdOrderByCreatedAtDesc(@Param("musicId") Integer musicId);
	Page<Review> findByMusicMusicIdOrderByScoreDesc(Integer musicId, Pageable pageable);
	
	Optional<Review> findByReviewId(Integer reviewId);
	
//	@Query("SELECT AVG(r.score) FROM Review r WHERE r.music.musicId = :musicId");

}

package com.example.musiccollection.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.musiccollection.entity.Genre;
import com.example.musiccollection.entity.Music;

public interface MusicRepository extends JpaRepository<Music, Integer>{
	@Query("SELECT m FROM Music m WHERE "
		     + "(LOWER(m.songTitle) LIKE %:songTitle% OR :songTitle IS NULL OR :songTitle = '') AND "
		     + "(LOWER(m.artist) LIKE %:artist% OR :artist IS NULL OR :artist = '')")
      public Page<Music> findBySongTitleOrArtistLike(@Param("songTitle") String songTitle, @Param("artist") String artist, Pageable pageable);
	
	public Page<Music> findBySongTitleOrArtistLikeOrderByCreatedAtDesc(@Param("songTitle") String songTitle, @Param("artist") String artist, Pageable pageable);
	public Page<Music> findBySongTitleOrArtistLikeOrderByPriceAsc(@Param("songTitle") String songTitle, @Param("artist") String artist, Pageable pageable);
	public Page<Music> findByGenreOrderByCreatedAtDesc(Genre genre, Pageable pageable);
	public Page<Music> findByGenreOrderByPriceAsc(Genre genre, Pageable pageable);
	public Page<Music> findByPriceLessThanEqualOrderByCreatedAtDesc(Integer price, Pageable pageable);
	public Page<Music> findByPriceLessThanEqualOrderByPriceAsc(Integer price, Pageable pageable);
	public Page<Music> findAllByOrderByCreatedAtDesc(Pageable pageable);
	public Page<Music> findAllByOrderByPriceAsc(Pageable pageable);
	
	public List<Music> findTop10ByOrderByCreatedAtDesc();

	Optional <Music> findByMusicId(Integer musicId);
}
package com.example.musiccollection.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.musiccollection.entity.Favorite;
import com.example.musiccollection.entity.Music;
import com.example.musiccollection.entity.User;
import com.example.musiccollection.repository.FavoriteRepository;
import com.example.musiccollection.repository.MusicRepository;
import com.example.musiccollection.repository.UserRepository;

@Service
public class FavoriteService {
	private final FavoriteRepository favoriteRepository;  
    private final MusicRepository musicRepository;  
    private final UserRepository userRepository;  
    
    public FavoriteService(FavoriteRepository favoriteRepository, MusicRepository musicRepository, UserRepository userRepository) {
        this.favoriteRepository = favoriteRepository;  
        this.musicRepository = musicRepository;  
        this.userRepository = userRepository;  
    }
    
    public Page<Music> getFavoritesByUserId(Integer userId, Pageable pageable) {
        // ユーザーのお気に入りの音楽を取得するロジック
        return favoriteRepository.findByFavoriteByUserId(userId, pageable);
    }
    
    public boolean isFavorite(Integer userId, Integer musicId) {
        return favoriteRepository.existsByUserIdAndMusicId(userId, musicId);
    }
    
    @Transactional
    public void addFavorite(Integer musicId, Integer userId) {
    	if (!isFavorite(userId, musicId)) {
            User user = userRepository.findByUserId(userId);
            Music music = musicRepository.findByMusicId(musicId).orElseThrow(() -> new RuntimeException("Music not found"));

            Favorite favorite = new Favorite();
            favorite.setUser(user);
            favorite.setMusic(music);
            favoriteRepository.save(favorite);
        }
    }

    @Transactional
    public void removeFavorite(Integer musicId, Integer userId) {
    	 Optional<Favorite> favorite = favoriteRepository.findByUserIdAndMusicId(userId, musicId);
         favorite.ifPresent(favoriteRepository::delete);

    }
}

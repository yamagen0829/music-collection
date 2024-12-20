package com.example.musiccollection.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.musiccollection.entity.Music;
import com.example.musiccollection.entity.Review;
import com.example.musiccollection.entity.User;
import com.example.musiccollection.form.ReviewForm;
import com.example.musiccollection.repository.MusicRepository;
import com.example.musiccollection.repository.ReviewRepository;
import com.example.musiccollection.repository.UserRepository;

import jakarta.validation.Valid;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final MusicRepository musicRepository;
    
    public ReviewService (ReviewRepository reviewRepository, UserRepository userRepository, MusicRepository musicRepository) {
    	this.reviewRepository = reviewRepository;
    	this.userRepository = userRepository;
    	this.musicRepository = musicRepository;
    }
    
    public void saveReview(Integer musicId, ReviewForm reviewForm, String userEmail) {
    	Optional<User> optionalUser = userRepository.findByUserEmail(userEmail);
//    	Music music = musicRepository.findByMusicId(musicId);
    	
    	Optional<Music> optionalMusic = musicRepository.findByMusicId(musicId);

        if (optionalUser.isPresent() && optionalMusic.isPresent()) {
            User user = optionalUser.get();
            Music music = optionalMusic.get();
    	
	    	Review review = new Review();
	    	review.setMusic(music);
	    	review.setScore(reviewForm.getScore());
	    	review.setContent(reviewForm.getContent());
			review.setUser(user);
	    	
	    	reviewRepository.save(review);
        }
    }
    
    public void updateReview(Integer reviewId, @Valid ReviewForm reviewForm, String email) {
		Optional<User> optionalUser = userRepository.findByUserEmail(email);
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);

        if (optionalUser.isPresent() && optionalReview.isPresent()) {
            User user = optionalUser.get();
            Review review = optionalReview.get();

            review.setScore(reviewForm.getScore());
            review.setContent(reviewForm.getContent());
           
            reviewRepository.save(review);
        } else {
            throw new IllegalStateException("ログインユーザーまたはレビューが見つかりません。");
        }
	}    
}

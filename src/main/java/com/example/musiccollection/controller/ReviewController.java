package com.example.musiccollection.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.musiccollection.entity.Music;
import com.example.musiccollection.entity.Review;
import com.example.musiccollection.entity.User;
import com.example.musiccollection.form.ReviewForm;
import com.example.musiccollection.repository.ReviewRepository;
import com.example.musiccollection.repository.UserRepository;
import com.example.musiccollection.security.UserDetailsImpl;
import com.example.musiccollection.service.MusicService;
import com.example.musiccollection.service.ReviewService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/reviews")
public class ReviewController {
	private final ReviewService reviewService;
    private final ReviewRepository reviewRepository;
    private final MusicService musicService;
    private final UserRepository userRepository;
    
    public ReviewController(ReviewService reviewService, ReviewRepository reviewRepository, MusicService musicService, UserRepository userRepository) {
    	this.reviewService = reviewService;
    	this.reviewRepository = reviewRepository;
    	this.musicService = musicService;
    	this.userRepository = userRepository;
    }
    
    // 現在のユーザーが有料会員かどうかを確認するメソッド
    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
    
    private User getCurrentUser() {
        String userEmail = getCurrentUsername();
        Optional<User> optionalUser = userRepository.findByUserEmail(userEmail);
        return optionalUser.orElse(null);
    }
    
    private boolean isPaidUser() {
        User user = getCurrentUser();
        return user != null && Boolean.TRUE.equals(user.getPaid());
    }
    
    @GetMapping("/postReviewForm/{musicId}")
    public String postReviewForm(@PathVariable("musicId") Integer musicId,
    		                     @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
    		                     Model model) {
    	if (!isPaidUser()) {
            return "redirect:/user/paid"; // 有料会員ページにリダイレクト
        }
    	
    	 // ログインしているユーザー情報を取得
        User currentUser = getCurrentUser();
        boolean isPaidUser = false;
        
        if (currentUser != null) {
            isPaidUser = currentUser.getPaid() != null? currentUser.getPaid() : false;
        } 
    	
    	  model.addAttribute("reviewForm", new ReviewForm());
    	  model.addAttribute("music", musicService.findByMusicId(musicId));
    	  model.addAttribute("isPaidUser", isPaidUser);
    	  
    	  return "reviews/review_post";
    }
    
    @PostMapping("/submitReview/{musicId}")
    public String submitReview(@PathVariable("musicId") Integer musicId,
    		                   @RequestParam("redirectUrl") String redirectUrl,
    		                   @ModelAttribute @Valid ReviewForm reviewForm, 
    		                   BindingResult result, 
    		                   Model model) {
    	if (!isPaidUser()) {
            return "redirect:/user/paid"; // 有料会員ページにリダイレクト
        }
    	
    	if (result.hasErrors()) {
            model.addAttribute("music", musicService.findByMusicId(musicId));
            model.addAttribute("redirectUrl", redirectUrl);
            return "reviews/review_post";
        }
    	// カンマが含まれていた場合の対策 (予備的対策)
        redirectUrl = redirectUrl.replaceAll(",$", "");

        reviewService.saveReview(musicId, reviewForm, getCurrentUsername());
      
        return "redirect:" + redirectUrl;
    }

    @GetMapping("/editReviewForm/{reviewId}")
    public String editReviewForm(@PathVariable("reviewId") Integer reviewId, Model model) {
    	if (!isPaidUser()) {
            return "redirect:/user/paid"; // 有料会員ページにリダイレクト
        }
    	Review review = reviewRepository.findByReviewId(reviewId).orElseThrow(() -> new IllegalArgumentException("Invalid review Id:" + reviewId));
    	Music music = review.getMusic();
    	
    	// 評価やコメントの事前設定
        ReviewForm reviewForm = new ReviewForm();
        reviewForm.setScore(review.getScore());
        reviewForm.setContent(review.getContent());


        model.addAttribute("reviewForm", reviewForm);
        model.addAttribute("review", review);
        model.addAttribute("music", music);
        return "reviews/review_edit";
    }

    @PostMapping("/updateReview/{reviewId}")
    public String updateReview(@PathVariable("reviewId") Integer reviewId,
    		                   @RequestParam("redirectUrl") String redirectUrl,
    		                   @ModelAttribute @Valid ReviewForm reviewForm, 
    		                   BindingResult result, 
    		                   Model model) {
    	if (!isPaidUser()) {
            return "redirect:/user/paid"; // 有料会員ページにリダイレクト
        }
    	
    	if (result.hasErrors()) {
    		Review review = reviewRepository.findByReviewId(reviewId).orElseThrow(() -> new IllegalArgumentException("Invalid review Id:" + reviewId));
            model.addAttribute("review", review);
            model.addAttribute("redirectUrl", redirectUrl);
            return "reviews/review_edit";
        }
    	
    	// カンマが含まれていた場合の対策 (予備的対策)
        redirectUrl = redirectUrl.replaceAll(",$", "");

        reviewService.updateReview(reviewId, reviewForm, getCurrentUsername());
       
        return "redirect:" + redirectUrl;
    }
    
    @PostMapping("/deleteReview/{reviewId}")
    public String deleteReview(@PathVariable("reviewId") Integer reviewId, 
                               @RequestParam("redirectUrl") String redirectUrl,
                               RedirectAttributes redirectAttributes) {
    	 Optional<Review> optionalReview = reviewRepository.findByReviewId(reviewId);
    	    
    	    if(optionalReview.isPresent()) {
    	        Review review = optionalReview.get();
    	        reviewRepository.deleteById(reviewId);
    	        
    	        redirectAttributes.addFlashAttribute("successMessage", "レビューを削除しました。");
    	        return "redirect:" + redirectUrl;
    	    } else {
    	        redirectAttributes.addFlashAttribute("errorMessage", "レビューが見つかりませんでした。");
    	        return "redirect:/musics";
    	    }
    }

    @GetMapping("/reviewList/{musicId}")
    public String reviewList(@PathVariable("musicId") Integer musicId, Model model, @PageableDefault(page = 0, size = 10, sort = "review_id", direction = Direction.DESC) Pageable pageable) {
//        Page<Review> reviewPage = reviewRepository.findByReviewIdOrderByCreatedAtDesc(reviewId, pageable);
        Page<Review> reviewPage = reviewRepository.findByMusicId(musicId, pageable);
        Music music = musicService.findByMusicId(musicId);
        
        // ログインしているユーザー情報を取得
        User currentUser = getCurrentUser();
        boolean isPaidUser = false;
        
        if (currentUser != null) {
            isPaidUser = currentUser.getPaid() != null? currentUser.getPaid() : false;
        }

        model.addAttribute("reviewPage", reviewPage);
        model.addAttribute("music", music);
        model.addAttribute("isPaidUser", isPaidUser);

        return "reviews/review";
    }
}

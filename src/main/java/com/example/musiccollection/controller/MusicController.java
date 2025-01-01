package com.example.musiccollection.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.musiccollection.entity.Genre;
import com.example.musiccollection.entity.Music;
import com.example.musiccollection.entity.Review;
import com.example.musiccollection.entity.User;
import com.example.musiccollection.form.UserMusicEditForm;
import com.example.musiccollection.form.UserMusicRegisterForm;
import com.example.musiccollection.repository.GenreRepository;
import com.example.musiccollection.repository.MusicRepository;
import com.example.musiccollection.repository.ReviewRepository;
import com.example.musiccollection.repository.UserRepository;
import com.example.musiccollection.service.FavoriteService;
import com.example.musiccollection.service.MusicService;

@Controller
@RequestMapping("/musics")
public class MusicController {
    private final MusicRepository musicRepository;
    private final GenreRepository genreRepository;
    private final MusicService musicService;
    private final ReviewRepository reviewRepository;
    private final FavoriteService favoriteService;
    private final UserRepository userRepository;
    
    public MusicController(MusicRepository musicRepository, GenreRepository genreRepository, MusicService musicService, ReviewRepository reviewRepository, FavoriteService favoriteService, UserRepository userRepository) {
    	this.musicRepository = musicRepository;
    	this.genreRepository = genreRepository;
    	this.musicService = musicService;
    	this.reviewRepository = reviewRepository;
    	this.favoriteService = favoriteService;
    	this.userRepository = userRepository;
    }
    
    @GetMapping
    public String index(@RequestParam(name = "songTitle", required = false) String songTitle,
    		            @RequestParam(name = "artist", required = false) String artist,
    		            @RequestParam(name = "genre", required = false) String genre,
    		            @RequestParam(name = "price", required = false) Integer price,
    		            @RequestParam(name = "order", required = false) String order,
    		            @PageableDefault(page = 0, size = 10, sort = "musicId", direction = Direction.ASC) Pageable pageable,
    		            Model model)
    {
    	
    	Page<Music> musicPage;
    	
    	 if ((songTitle != null && !songTitle.isEmpty()) || (artist != null && !artist.isEmpty())) {
	    	 if (order != null && order.equals("priceAsc")) {	
    		     if (songTitle != null && !songTitle.isEmpty()) {
	    			 songTitle = songTitle.toLowerCase();
	    		 }
	    		 if (artist != null && !artist.isEmpty()) {
	    			 artist = artist.toLowerCase();
	    		 }
	    		 musicPage = musicRepository.findBySongTitleOrArtistLikeOrderByPriceAsc(songTitle, artist, pageable);
	    	 } else {
	    		 if (songTitle != null && !songTitle.isEmpty()) {
	    			 songTitle = songTitle.toLowerCase();
	    		 }
	    		 if (artist != null && !artist.isEmpty()) {
	    			 artist = artist.toLowerCase();
	    		 }
	    		 musicPage = musicRepository.findBySongTitleOrArtistLikeOrderByCreatedAtDesc(songTitle, artist, pageable);
	    	 }
    		 
    	 } else if (genre != null && !genre.isEmpty()) {
    		 if (order != null && order.equals("priceAsc")) {
	    		 Optional<Genre> genreEntity = genreRepository.findByGenreName(genre);
	    		 if (genreEntity.isPresent()) {
	    		     musicPage = musicRepository.findByGenreOrderByPriceAsc(genreEntity.get(), pageable);	 
	    		 } else {
	    			 musicPage = Page.empty(pageable);
	    		 }
    		 } else {
    			 Optional<Genre> genreEntity = genreRepository.findByGenreName(genre);
	    		 if (genreEntity.isPresent()) {
    			     musicPage = musicRepository.findByGenreOrderByCreatedAtDesc(genreEntity.get(), pageable);
	    		 } else {
	    			 musicPage = Page.empty(pageable);
	    		 }
    		 }
         } else if (price != null) {
        	 if (order != null && order.equals("priceAsc")) {
    		     musicPage = musicRepository.findByPriceLessThanEqualOrderByPriceAsc(price, pageable);
        	 } else {
        		 musicPage = musicRepository.findByPriceLessThanEqualOrderByCreatedAtDesc(price, pageable);
        	 }
    	 } else {
    		 if (order != null && order.equals("priceAsc")) {
    		     musicPage = musicRepository.findAllByOrderByPriceAsc(pageable);
    		 } else {
    			 musicPage = musicRepository.findAllByOrderByCreatedAtDesc(pageable);
    		 }
    	 }
    	 
    	 User currentUser = getCurrentUser();
         boolean isPaidUser = false;
         
         if (currentUser != null) {
             isPaidUser = currentUser.getPaid() != null? currentUser.getPaid() : false;
         }
    	 
    	 model.addAttribute("musicPage", musicPage);
    	 model.addAttribute("songTitle", songTitle);
    	 model.addAttribute("artist", artist);
    	 model.addAttribute("genre", genre);
    	 model.addAttribute("price", price);
    	 model.addAttribute("order", order);
    	 model.addAttribute("isPaidUser", isPaidUser);
    	 
    	 return "musics/index";
    }
    
    @GetMapping("/{musicId}")
    public String userMusicShow(@PathVariable(name = "musicId") Integer musicId, Integer userId, Model model, Authentication authentication) {
    	Music music = musicRepository.getReferenceById(musicId);
    	boolean canEditOrDelete = musicService.canEditOrDelete(musicId, authentication);
    	List<Review> reviews = reviewRepository.findTop6ByMusicMusicIdOrderByCreatedAtDesc(musicId);
    	
    	 // ログインしているユーザー情報を取得
        User currentUser = getCurrentUser();
        boolean isFavorite = false;
        boolean isPaidUser = false;
        
        if (currentUser != null) {
            isFavorite = favoriteService.isFavorite(currentUser.getUserId(), musicId);
            isPaidUser = currentUser.getPaid() != null? currentUser.getPaid() : false;
        }
    	
    	model.addAttribute("music", music);
    	model.addAttribute("canEditOrDelete", canEditOrDelete);
    	model.addAttribute("reviews", reviews);
    	model.addAttribute("isFavorite", isFavorite);
    	model.addAttribute("isPaidUser", isPaidUser);
    	
    	return "musics/music_show";
    }
    
    @GetMapping("/userMusicRegister")
    public String userMusicRegister(Model model) {
    	model.addAttribute("userMusicRegisterForm", new UserMusicRegisterForm());
    	return "musics/music_register";
    }
    
    @PostMapping("/userMusicCreate")
    public String userMusicCreate(@ModelAttribute @Validated UserMusicRegisterForm userMusicRegisterForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
    	if (bindingResult.hasErrors()) {
    		return "musics/music_register";
    	}
    	
    	musicService.userCreate(userMusicRegisterForm);
    	redirectAttributes.addFlashAttribute("successMessage", "曲を登録しました。");
    	
    	return "redirect:/musics";
    }
    
    @GetMapping("/{musicId}/userMusicEdit")
    public String userMusicEdit(@PathVariable(name = "musicId") Integer musicId, Model model) {
    	Music music = musicRepository.getReferenceById(musicId);
        String imageName = music.getImageName();
        String musicFile = music.getMusicFile();
        UserMusicEditForm userMusicEditForm = new UserMusicEditForm(music.getMusicId(), music.getSongTitle(), music.getArtist(), null, null, music.getDescription(), music.getGenreName(), music.getPrice(), music.getEmail());
        
        model.addAttribute("music", music);
        model.addAttribute("imageName", imageName);
        model.addAttribute("musicFile", musicFile);
        model.addAttribute("userMusicEditForm", userMusicEditForm);
        
        return "musics/music_edit";
    }
    
    @PostMapping("/{musicId}/userMusicUpdate")
    public String userMusicUpdate(@ModelAttribute @Validated UserMusicEditForm userMusicEditForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
    	if (bindingResult.hasErrors()) {
    		return "musics/music_edit";
    	}
    	
    	musicService.userUpdate(userMusicEditForm);
    	redirectAttributes.addFlashAttribute("successMessage", "曲情報を編集しました。");
    	
    	return "redirect:/musics";
    }
    
    @PostMapping("/{musicId}/userMusicDelete")
    public String userMusicDelete(@PathVariable(name = "musicId") Integer musicId, RedirectAttributes redirectAttributes) {
    	musicRepository.deleteById(musicId);
    	
    	redirectAttributes.addFlashAttribute("successMessage", "曲を削除しました。");
    	
    	return "redirect:/musics";
    }
    
    @PostMapping("/{musicId}/addFavorite")
    public String addFavorite(@PathVariable(name = "musicId") Integer musicId, Integer userId, RedirectAttributes redirectAttributes) {
    	User currentUser = getCurrentUser();
    	
    	if (currentUser == null || !Boolean.TRUE.equals(currentUser.getPaid())) {
            return "redirect:/user/paid"; // 有料会員ページにリダイレクト
        }

    	try {    
	            favoriteService.addFavorite(musicId, currentUser.getUserId());
	            redirectAttributes.addFlashAttribute("message", "お気に入りに追加しました。");
	        } catch (Exception e) {	
	        	e.printStackTrace();	
	        	redirectAttributes.addFlashAttribute("errorMessage", "お気に入りの追加中にエラーが発生しました: " + e.getMessage());	
	        }
	
	        return "redirect:/musics/" + musicId;
    }

    @PostMapping("/{musicId}/removeFavorite")
    public String removeFavorite(@PathVariable(name = "musicId") Integer musicId, Integer userId, RedirectAttributes redirectAttributes) {
    	User currentUser = getCurrentUser();
    	if (currentUser == null || !Boolean.TRUE.equals(currentUser.getPaid())) {
            return "redirect:/user/paid"; // 有料会員ページにリダイレクト
        }
    
        try {
            favoriteService.removeFavorite(musicId, currentUser.getUserId());
            redirectAttributes.addFlashAttribute("message", "お気に入りを解除しました。");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "お気に入りの解除中にエラーが発生しました: " + e.getMessage());
        }

        return "redirect:/musics/" + musicId;
    }
    
    private User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String userEmail = ((UserDetails) principal).getUsername();
            Optional<User> optionalUser = userRepository.findByUserEmail(userEmail);
            return optionalUser.orElse(null);
        }
        return null;
    }
}

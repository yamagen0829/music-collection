package com.example.musiccollection.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.musiccollection.entity.Music;
import com.example.musiccollection.entity.User;
import com.example.musiccollection.repository.MusicRepository;
import com.example.musiccollection.repository.UserRepository;

@Controller
public class HomeController {
	private final MusicRepository musicRepository;
	private final UserRepository userRepository;
	
	public HomeController(MusicRepository musicRepository, UserRepository userRepository) {
		this.musicRepository = musicRepository;
		this.userRepository = userRepository;
	}
	
	@GetMapping("/")
    public String index(Model model) {
		List<Music> newMusics = musicRepository.findTop10ByOrderByCreatedAtDesc();
		
		 User currentUser = getCurrentUser();
         boolean isPaidUser = false;
         
         if (currentUser != null) {
             isPaidUser = currentUser.getPaid() != null? currentUser.getPaid() : false;
         }
         
		model.addAttribute("newMusics", newMusics);
		model.addAttribute("isPaidUser", isPaidUser);
		
		//　リストをシャッフルしてランダムに並び替える
		Collections.shuffle(newMusics);
		
        return "index";
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

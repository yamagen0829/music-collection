package com.example.musiccollection.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.musiccollection.entity.Music;
import com.example.musiccollection.entity.User;
import com.example.musiccollection.repository.UserRepository;
import com.example.musiccollection.security.UserDetailsImpl;
import com.example.musiccollection.service.FavoriteService;
import com.example.musiccollection.service.UserService;

@Controller
@RequestMapping("/favorites")
public class FavoriteController {
	private final FavoriteService favoriteService;
    private final UserService userService;
    private final UserRepository userRepository;

    public FavoriteController(FavoriteService favoriteService, UserService userService, UserRepository userRepository) {
        this.favoriteService = favoriteService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/showFavorites")
    public String showFavorites(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, @RequestParam(defaultValue = "0") int page, Model model, RedirectAttributes redirectAttributes) {
    	try {
    		// ログインしているユーザー情報を取得
            User currentUser = getCurrentUser();
            if (currentUser == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "ユーザー情報が取得できませんでした。再度ログインしてください。");
                return "redirect:/login"; // ログインページにリダイレクト
            }

            boolean isPaidUser = currentUser.getPaid() != null ? currentUser.getPaid() : false;
            if (!isPaidUser) {
                redirectAttributes.addFlashAttribute("errorMessage", "この機能を利用するには有料会員になる必要があります。");
                return "redirect:/user/paid"; // 有料会員ページにリダイレクト
            }
	            Pageable pageable = PageRequest.of(page, 10); // 1ページあたり10件表示
	            Page<Music> favoritePage = favoriteService.getFavoritesByUserId(currentUser.getUserId(), pageable); 
	            
	            if (favoritePage.isEmpty()) {
	            	model.addAttribute("noFavoritesMessage", "お気に入り登録がありませんでした。");
	            }
	            
	            model.addAttribute("favoritePage", favoritePage);
	            model.addAttribute("isPaidUser", isPaidUser);
	        
        } catch (Exception e) {
    		  // エラーログを出力
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "お気に入りの取得中にエラーが発生しました: " + e.getMessage());
            return "redirect:/errorPage";
        }
	        return "musics/favorites";  // お気に入り一覧を表示するためのThymeleafテンプレート
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

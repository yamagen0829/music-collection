package com.example.musiccollection.controller;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.musiccollection.entity.Music;
import com.example.musiccollection.repository.MusicRepository;

@Controller
public class HomeController {
	private final MusicRepository musicRepository;
	
	// コントローラー内にロガーを追加
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	
	public HomeController(MusicRepository musicRepository) {
		this.musicRepository = musicRepository;
	}
	
	@GetMapping("/")
    public String index(Model model) {
		List<Music> newMusics = musicRepository.findTop10ByOrderByCreatedAtDesc();
		model.addAttribute("newMusics", newMusics);
		
		//　リストをシャッフルしてランダムに並び替える
		Collections.shuffle(newMusics);
		
		// ログ出力
	    logger.info("Retrieved {} new musics", newMusics.size());
	    for (Music music : newMusics) {
	        logger.info("Music ID: {}, Image Name: {}", music.getMusicId(), music.getImageName());
	    }
		
        return "index";
    }
}

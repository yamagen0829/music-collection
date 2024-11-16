package com.example.musiccollection.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
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

import com.example.musiccollection.entity.Music;
import com.example.musiccollection.form.MusicEditForm;
import com.example.musiccollection.form.MusicRegisterForm;
import com.example.musiccollection.repository.GenreRepository;
import com.example.musiccollection.repository.MusicRepository;
import com.example.musiccollection.service.MusicService;

@Controller
@RequestMapping("/admin/musics")
public class AdminMusicController {
     private final MusicRepository musicRepository;
     private final GenreRepository genreRepository;
     private final MusicService musicService;
     
     public AdminMusicController(MusicRepository musicRepository, GenreRepository genreRepository, MusicService musicService) {
    	 this.musicRepository = musicRepository;
    	 this.genreRepository = genreRepository;
    	 this.musicService = musicService;
     }
     
     @GetMapping
     public String index(Model model, @PageableDefault(page = 0, size = 10, sort = "musicId", direction = Direction.ASC) Pageable pageable, @RequestParam(name = "songTitle", required = false) String songTitle, @RequestParam(name = "artist", required = false) String artist) {
    	 Page<Music> musicPage;
    	 
    	 if ((songTitle != null && !songTitle.isEmpty()) || (artist != null && !artist.isEmpty())) {
    		 if (songTitle != null && !songTitle.isEmpty()) {
    			 songTitle = songTitle.toLowerCase();
    		 }
    		 if (artist != null && !artist.isEmpty()) {
    			 artist = artist.toLowerCase();
    		 }
    		 
    		 musicPage = musicRepository.findBySongTitleOrArtistLike(songTitle, artist, pageable);
    	 } else {
    		 musicPage = musicRepository.findAll(pageable);
    	 }
    	 
    	 model.addAttribute("musicPage", musicPage);
    	 model.addAttribute("songTitle", songTitle);
    	 model.addAttribute("artist", artist);
    	 
    	 return "admin/musics/index";
     }
     
    @GetMapping("/{musicId}")
    public String musicShow(@PathVariable(name = "musicId") Integer musicId, Model model) {
    	Music music = musicRepository.getReferenceById(musicId);
    	
        model.addAttribute("music", music);
        
        return "admin/musics/music_show";
    }
    
    @GetMapping("/musicRegister")
    public String musicRegister(Model model) {
    	model.addAttribute("musicRegisterForm", new MusicRegisterForm());
    	return "admin/musics/music_register";
    }
    
    @PostMapping("/musicCreate")
    public String musicCreate(@ModelAttribute @Validated MusicRegisterForm musicRegisterForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
    	if (bindingResult.hasErrors()) {
    		return "admin/musics/music_register";
    	}
    	
    	musicService.create(musicRegisterForm);
    	redirectAttributes.addFlashAttribute("successMessage", "曲を登録しました。");
    	
    	return "redirect:/admin/musics";
    }
    
    @GetMapping("/{musicId}/musicEdit")
    public String musicEdit(@PathVariable(name = "musicId") Integer musicId, Model model) {
    	Music music = musicRepository.getReferenceById(musicId);
        String imageName = music.getImageName();
        String musicFile = music.getMusicFile();
        MusicEditForm musicEditForm = new MusicEditForm(music.getMusicId(), music.getSongTitle(), music.getArtist(), null, null, music.getDescription(), music.getGenreName(), music.getPrice(), music.getEmail());
        
        model.addAttribute("music", music);
        model.addAttribute("imageName", imageName);
        model.addAttribute("musicFile", musicFile);
        model.addAttribute("musicEditForm", musicEditForm);
        
        return "admin/musics/music_edit";
    }
    
    @PostMapping("/{musicId}/musicUpdate")
    public String musicUpdate(@ModelAttribute @Validated MusicEditForm musicEditForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
    	if (bindingResult.hasErrors()) {
    		return "admin/musics/music_edit";
    	}
    	
    	musicService.update(musicEditForm);
    	redirectAttributes.addFlashAttribute("successMessage", "曲情報を編集しました。");
    	
    	return "redirect:/admin/musics";
    }
    
    @PostMapping("/{musicId}/musicDelete")
    public String musicDelete(@PathVariable(name = "musicId") Integer musicId, RedirectAttributes redirectAttributes) {
    	musicRepository.deleteById(musicId);
    	
    	redirectAttributes.addFlashAttribute("successMessage", "曲を削除しました。");
    	
    	return "redirect:/admin/musics";
    }
}

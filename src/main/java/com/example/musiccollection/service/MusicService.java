package com.example.musiccollection.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.musiccollection.entity.Genre;
import com.example.musiccollection.entity.Music;
import com.example.musiccollection.form.MusicEditForm;
import com.example.musiccollection.form.MusicRegisterForm;
import com.example.musiccollection.form.UserMusicEditForm;
import com.example.musiccollection.form.UserMusicRegisterForm;
import com.example.musiccollection.repository.GenreRepository;
import com.example.musiccollection.repository.MusicRepository;

@Service
public class MusicService {
     private final MusicRepository musicRepository;
     private final GenreRepository genreRepository;
     
     public MusicService(MusicRepository musicRepository, GenreRepository genreRepository) {
    	 this.musicRepository = musicRepository;
    	 this.genreRepository = genreRepository;
     }
     
     @Transactional
     public void create(MusicRegisterForm musicRegisterForm) {
    	 Music music = new Music();
    	 MultipartFile imageFile = musicRegisterForm.getImageFile();
    	 MultipartFile musicFile = musicRegisterForm.getMusicFile();
    	 
    	 if (!imageFile.isEmpty()) {
    		 String imageName = imageFile.getOriginalFilename();
    		 String hashedImageName = generateNewFileName(imageName);
    		 Path filePath = Paths.get("src/main/resources/static/storage/" + hashedImageName);
    		 copyImageFile(imageFile, filePath);
    		 music.setImageName(hashedImageName);
    	 }
    	 
    	 if (!musicFile.isEmpty()) {
    		 String musicFileName = musicFile.getOriginalFilename();
    		 String hashedMusicFileName = generateNewFileName(musicFileName);
    		 Path filePath = Paths.get("src/main/resources/static/music/" + hashedMusicFileName);
    		 copyMusicFile(musicFile, filePath);
    		 music.setMusicFile(hashedMusicFileName);
    	 }
    	 
    	 //ジャンルの処理
    	 String genreName = musicRegisterForm.getGenreName();
    	 Genre genre = genreRepository.findByGenreName(genreName)
    			 .orElseGet(() -> {
    				 Genre newGenre = new Genre();
    				 newGenre.setGenreName(genreName);
    				 return genreRepository.save(newGenre);
    			 });
    	 
    	 music.setSongTitle(musicRegisterForm.getSongTitle());
    	 music.setArtist(musicRegisterForm.getArtist());
    	 music.setDescription(musicRegisterForm.getDescription());
    	 music.setGenre(genre);
    	 music.setPrice(musicRegisterForm.getPrice());
    	 music.setEmail(musicRegisterForm.getEmail());
    	 
    	 musicRepository.save(music);
     }
     
     @Transactional
     public void userCreate(UserMusicRegisterForm userMusicRegisterForm) {
    	 Music music = new Music();
    	 MultipartFile imageFile = userMusicRegisterForm.getImageFile();
    	 MultipartFile musicFile = userMusicRegisterForm.getMusicFile();
    	 
    	 if (!imageFile.isEmpty()) {
    		 String imageName = imageFile.getOriginalFilename();
    		 String hashedImageName = generateNewFileName(imageName);
    		 Path filePath = Paths.get("src/main/resources/static/storage/" + hashedImageName);
    		 copyImageFile(imageFile, filePath);
    		 music.setImageName(hashedImageName);
    	 }
    	 
    	 if (!musicFile.isEmpty()) {
    		 String musicFileName = musicFile.getOriginalFilename();
    		 String hashedMusicFileName = generateNewFileName(musicFileName);
    		 Path filePath = Paths.get("src/main/resources/static/music/" + hashedMusicFileName);
    		 copyMusicFile(musicFile, filePath);
    		 music.setMusicFile(hashedMusicFileName);
    	 }
    	 
    	 //ジャンルの処理
    	 String genreName = userMusicRegisterForm.getGenreName();
    	 Genre genre = genreRepository.findByGenreName(genreName)
    			 .orElseGet(() -> {
    				 Genre newGenre = new Genre();
    				 newGenre.setGenreName(genreName);
    				 return genreRepository.save(newGenre);
    			 });
    	 
    	 music.setSongTitle(userMusicRegisterForm.getSongTitle());
    	 music.setArtist(userMusicRegisterForm.getArtist());
    	 music.setDescription(userMusicRegisterForm.getDescription());
    	 music.setGenre(genre);
    	 music.setPrice(userMusicRegisterForm.getPrice());
    	 music.setEmail(userMusicRegisterForm.getEmail());
    	 
    	 musicRepository.save(music);
     }
     
     //ユーザー情報を取得
     private String getCurrentUserEmail(Authentication authentication) {
    	 if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
    		 UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    		 return userDetails.getUsername();
    	 }
    	 return null;
     }
     
     //管理者権限を持っているかを確認
     private boolean isAdmin(Authentication authentication) {
    	 return authentication.getAuthorities().stream()
    			 .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
    	 }
     
     //曲の編集が許可されているかどうかを確認
     public boolean canEditOrDelete(Integer musicId, Authentication authentication) {
    	 Music music = musicRepository.getReferenceById(musicId);
    	 String currentUserEmail = getCurrentUserEmail(authentication);
    	 
    	 //曲のオーナーか管理者のみが許可されます
    	 return music.getEmail().equals(currentUserEmail) || isAdmin(authentication);
     }
     
     @Transactional
     public void update(MusicEditForm musicEditForm) {
    	 Music music = musicRepository.getReferenceById(musicEditForm.getMusicId());
    	 MultipartFile imageFile = musicEditForm.getImageFile();
    	 MultipartFile musicFile = musicEditForm.getMusicFile();
    	 
    	 if (!imageFile.isEmpty()) {
    		 String imageName = imageFile.getOriginalFilename();
    		 String hashedImageName = generateNewFileName(imageName);
    		 Path filePath = Paths.get("src/main/resources/static/storage/" + hashedImageName);
    		 copyImageFile(imageFile, filePath);
    		 music.setImageName(hashedImageName);
    	 }
    	 
    	 if (!musicFile.isEmpty()) {
    		 String musicFileName = musicFile.getOriginalFilename();
    		 String hashedMusicFileName = generateNewFileName(musicFileName);
    		 Path filePath = Paths.get("src/main/resources/static/music/" + hashedMusicFileName);
    		 copyMusicFile(musicFile, filePath);
    		 music.setMusicFile(hashedMusicFileName);
    	 }
    	 
    	 //ジャンルの処理
    	 String genreName = musicEditForm.getGenreName();
    	 Genre genre = genreRepository.findByGenreName(genreName)
    			 .orElseGet(() -> {
    				 Genre newGenre = new Genre();
    				 newGenre.setGenreName(genreName);
    				 return genreRepository.save(newGenre);
    			 });
    	 
    	 music.setSongTitle(musicEditForm.getSongTitle());
    	 music.setArtist(musicEditForm.getArtist());
    	 music.setDescription(musicEditForm.getDescription());
    	 music.setGenre(genre);
    	 music.setPrice(musicEditForm.getPrice());
    	 music.setEmail(musicEditForm.getEmail());
    	 
    	 musicRepository.save(music);
     }
     
     @Transactional
     public void userUpdate(UserMusicEditForm userMusicEditForm) {
    	 Music music = musicRepository.getReferenceById(userMusicEditForm.getMusicId());
    	 MultipartFile imageFile = userMusicEditForm.getImageFile();
    	 MultipartFile musicFile = userMusicEditForm.getMusicFile();
    	 
    	 if (!imageFile.isEmpty()) {
    		 String imageName = imageFile.getOriginalFilename();
    		 String hashedImageName = generateNewFileName(imageName);
    		 Path filePath = Paths.get("src/main/resources/static/storage/" + hashedImageName);
    		 copyImageFile(imageFile, filePath);
    		 music.setImageName(hashedImageName);
    	 }
    	 
    	 if (!musicFile.isEmpty()) {
    		 String musicFileName = musicFile.getOriginalFilename();
    		 String hashedMusicFileName = generateNewFileName(musicFileName);
    		 Path filePath = Paths.get("src/main/resources/static/music/" + hashedMusicFileName);
    		 copyMusicFile(musicFile, filePath);
    		 music.setMusicFile(hashedMusicFileName);
    	 }
    	 
    	 //ジャンルの処理
    	 String genreName = userMusicEditForm.getGenreName();
    	 Genre genre = genreRepository.findByGenreName(genreName)
    			 .orElseGet(() -> {
    				 Genre newGenre = new Genre();
    				 newGenre.setGenreName(genreName);
    				 return genreRepository.save(newGenre);
    			 });
    	 
    	 music.setSongTitle(userMusicEditForm.getSongTitle());
    	 music.setArtist(userMusicEditForm.getArtist());
    	 music.setDescription(userMusicEditForm.getDescription());
    	 music.setGenre(genre);
    	 music.setPrice(userMusicEditForm.getPrice());
    	 music.setEmail(userMusicEditForm.getEmail());
    	 
    	 musicRepository.save(music);
     }
     
     public String generateNewFileName(String originalFileName) {
    	    String uuid = UUID.randomUUID().toString();
    	    int dotIndex = originalFileName.lastIndexOf(".");
    	    String extension = dotIndex == -1 ? "" :originalFileName.substring(dotIndex);
    	    String baseName = dotIndex == -1 ? originalFileName : originalFileName.substring(0, dotIndex);
    	    
    	    return baseName + "_" + uuid + extension;
     } 	 
   
     // 画像ファイルを指定したファイルにコピーする
     public void copyImageFile(MultipartFile imageFile, Path filePath) {           
    	 try {
             Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
         } catch (IOException e) {
             e.printStackTrace();
         } 
     } 
     
  // 音楽ファイルを指定したファイルにコピーする
     public void copyMusicFile(MultipartFile musicFile, Path filePath) {           
         try {
             Files.copy(musicFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
         } catch (IOException e) {
             e.printStackTrace();
         }          
     } 
}

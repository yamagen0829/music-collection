package com.example.musiccollection.form;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserMusicEditForm {
	    @NotNull
	    private Integer MusicId;
	    
	    @NotBlank(message = "曲名を入力してください。")
	    private String songTitle;
	    
	    @NotBlank(message = "アーティスト名を入力してください。")
	    private String artist;
	    
	    private MultipartFile imageFile;
	   
	    private MultipartFile musicFile;
	    
	    @NotBlank(message = "説明を入力してください。")
	    private String description;
	    
	    @NotBlank(message = "ジャンルを入力してください。")
	    private String genreName;
	    
	    @NotNull(message = "料金を入力してください。")
	    @Min(value = 1, message = "料金は1円以上を入力してください。")
	    private Integer price;
	    
	    @NotBlank(message = "連絡先を入力してください。")
	    private String email;    
}

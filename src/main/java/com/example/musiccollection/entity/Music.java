package com.example.musiccollection.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "musics")
@Data
public class Music {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "music_id")
    private Integer musicId;
    
    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;
    
    @Column(name = "song_title")
    private String songTitle;
    
    @Column(name = "artist")
    private String artist;
    
    @Column(name = "image_name")
    private String imageName;
    
    @Column(name = "music_file")
    private String musicFile;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "price")
    private Integer price;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private Timestamp updatedAt;
    
    public String getGenreName() {
    	return genre != null ? genre.getGenreName() : null;
    }

	public Music orElse(Object object) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}
}

package com.example.musiccollection.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.musiccollection.entity.Genre;

public interface GenreRepository extends JpaRepository<Genre, Integer>{
    Optional<Genre> findByGenreName(String genreName); 
}

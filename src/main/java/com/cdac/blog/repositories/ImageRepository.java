package com.cdac.blog.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cdac.blog.entities.Image;



public interface ImageRepository extends JpaRepository<Image, Long> {
	Optional<Image> findByPostId(Integer postId);
	
}

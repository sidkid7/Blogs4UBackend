package com.cdac.blog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cdac.blog.entities.Comment;

public interface CommentRepo  extends JpaRepository<Comment	, Integer> {
			
}
